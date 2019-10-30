package org.springblossom.core.secure.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springblossom.core.tool.utils.CollectionUtil;
import org.springblossom.core.tool.utils.ObjectUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * redis 存储session
 *
 * @author guolf
 */
@Slf4j
public class RedisSessionDAO extends AbstractSessionDAO implements SessionDAO {


	private RedisTemplate redisTemplate;
	private ShiroSecureProperties shiroSecureProperties;

	public RedisSessionDAO() {
	}

	public RedisSessionDAO(RedisTemplate redisTemplate, ShiroSecureProperties shiroSecureProperties) {
		this.redisTemplate = redisTemplate;
		this.shiroSecureProperties = shiroSecureProperties;
	}


	/**
	 * Subclass hook to actually persist the given <tt>Session</tt> instance to the underlying EIS.
	 *
	 * @param session the Session instance to persist to the EIS.
	 * @return the id of the session created in the EIS (i.e. this is almost always a primary key and should be the
	 * value returned from {@link Session#getId() Session.getId()}.
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.update(session);
		return sessionId;
	}

	/**
	 * Subclass implementation hook that retrieves the Session object from the underlying EIS or {@code null} if a
	 * session with that ID could not be found.
	 *
	 * @param sessionId the id of the <tt>Session</tt> to retrieve.
	 * @return the Session in the EIS identified by <tt>sessionId</tt> or {@code null} if a
	 * session with that ID could not be found.
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (Objects.isNull(sessionId)) {
			return null;
		}
		byte[] session = redisTemplate.getConnectionFactory()
			.getConnection().stringCommands().get(buildRedisSessionKey(sessionId));

		SimpleSession session1 = (SimpleSession) ObjectUtil.unserialize(session);

		return session1;
	}

	/**
	 * Updates (persists) data from a previously created Session instance in the EIS identified by
	 * {@code {@link Session#getId() session.getId()}}.  This effectively propagates
	 * the data in the argument to the EIS record previously saved.
	 * <p/>
	 * In addition to UnknownSessionException, implementations are free to throw any other
	 * exceptions that might occur due to integrity violation constraints or other EIS related
	 * errors.
	 *
	 * @param session the Session to update
	 * @throws UnknownSessionException if no existing EIS session record exists with the
	 *                                 identifier of {@link Session#getId() session.getSessionId()}
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || Objects.isNull(session.getId())) {
			return;
		}

		byte[] key = buildRedisSessionKey(session.getId());
		redisTemplate.getConnectionFactory().getConnection().stringCommands().set(key, ObjectUtil.serialize(session));
		redisTemplate.getConnectionFactory().getConnection().expire(key, shiroSecureProperties.getSessionTimeout() * 60);
	}

	/**
	 * Deletes the associated EIS record of the specified {@code session}.  If there never
	 * existed a session EIS record with the identifier of
	 * {@link Session#getId() session.getId()}, then this method does nothing.
	 *
	 * @param session the session to delete.
	 */
	@Override
	public void delete(Session session) {
		if (session == null || Objects.isNull(session.getId())) {
			return;
		}
		byte[] key = buildRedisSessionKey(session.getId());
		redisTemplate.getConnectionFactory().getConnection().del(key);
	}

	/**
	 * Returns all sessions in the EIS that are considered active, meaning all sessions that
	 * haven't been stopped/expired.  This is primarily used to validate potential orphans.
	 * <p/>
	 * If there are no active sessions in the EIS, this method may return an empty collection or {@code null}.
	 * <h4>Performance</h4>
	 * This method should be as efficient as possible, especially in larger systems where there might be
	 * thousands of active sessions.  Large scale/high performance
	 * implementations will often return a subset of the total active sessions and perform validation a little more
	 * frequently, rather than return a massive set and validate infrequently.  If efficient and possible, it would
	 * make sense to return the oldest unstopped sessions available, ordered by
	 * {@link Session#getLastAccessTime() lastAccessTime}.
	 * <h4>Smart Results</h4>
	 * <em>Ideally</em> this method would only return active sessions that the EIS was certain should be invalided.
	 * Typically that is any session that is not stopped and where its lastAccessTimestamp is older than the session
	 * timeout.
	 * <p/>
	 * For example, if sessions were backed by a relational database or SQL-92 'query-able' enterprise cache, you might
	 * return something similar to the results returned by this query (assuming
	 * {@link SimpleSession SimpleSession}s were being stored):
	 * <pre>
	 * select * from sessions s where s.lastAccessTimestamp < ? and s.stopTimestamp is null
	 * </pre>
	 * where the {@code ?} parameter is a date instance equal to 'now' minus the session timeout
	 * (e.g. now - 30 minutes).
	 *
	 * @return a Collection of {@code Session}s that are considered active, or an
	 * empty collection or {@code null} if there are no active sessions.
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		Collection<Session> sessions = Lists.newArrayList();
		Set<byte[]> keySet = redisTemplate.getConnectionFactory().getConnection().keyCommands().keys((shiroSecureProperties.getSessionKeyPrefix().concat("*")).getBytes());
		if (CollectionUtil.isNotEmpty(keySet)) {
			for (byte[] key : keySet) {
				byte[] sessionByte = redisTemplate.getConnectionFactory().getConnection().stringCommands().get(key);
				SimpleSession session = (SimpleSession) ObjectUtil.unserialize(sessionByte);
				sessions.add(session);
			}
		}
		return sessions;
	}

	private byte[] buildRedisSessionKey(Serializable sessionId) {
		return (shiroSecureProperties.getSessionKeyPrefix() + sessionId).getBytes();
	}
}
