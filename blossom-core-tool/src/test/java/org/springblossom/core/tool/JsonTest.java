package org.springblossom.core.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springblossom.core.tool.jackson.JsonUtil;
import org.springframework.web.util.HtmlUtils;
import springfox.documentation.spring.web.json.Json;

public class JsonTest {

	public static void main(String[] args) {
		ObjectMapper objectMapper = JsonUtil.getInstance();

		String json = "{\"title\":\"九类消防安全突出 风险 将被重点整治\",\"categoryId\":7,\"categoryName\":\"test\",\"type\":1,\"draftDatetime\":\"2019-05-18 00:00:00\",\"unitId\":\"\",\"cmsArticleInfo\":{\"content\":\"<p>央视网消息(新闻 联播)：中共中央总书记、国家主席、中央军委主席、中央全面深化改革委员会主任习近平5月29日下午主持召开中央全面深化改革委员会第八次会议并发表重要讲话。他强调，当前，我国改革发展形势正处于深刻变化之中，外部不确定不稳定因素增多，改革发展面临许多新情况新问题。我们要保持战略定力，坚持问题导向，因势利导、统筹谋划、精准施策，在防范化解重大矛盾和突出问题上出实招硬招，推动改革更好服务经济社会发展大局。</p>\\n<p>中共中央政治局常委、中央全面深化改革委员会副主任李克强、王沪宁、韩正出席 会议。</p>\\n<p class=\\\"f_center\\\"><img id=\\\"netease1559135300866142\\\" contenteditable=\\\"false\\\" src=\\\"http://dingyue.ws.126.net/cPhMSKDA9Cv=qOypnH46kpkVMs73FwOYGefwdihak0JSj1559135301247compressflag.jpg\\\" alt=\\\"习近平主持召开中央深改委第八次会议\\\" /></p>\\n<p>会议审议通过了《关于创新和完善宏观调控的指导意见》、《关 于在山西开展能源革命综合改革试点的意见》、《关于深化影视业综合改革促进我国影视业健康发展的意见》、《关于加强创新能力开放合作的若干意见》、《关于治理高值医用耗材的改革方案》、《关于改革完善体制机制加强粮食储备安全管理的若干意见》、《关于完善建设用地使用权转让、出租、抵押二级市场的指导意见》、《关于加快农业保险高质量发展的指导意见》、《关于进一步推进移风易俗建设文明乡风的指导意见》和《关于各地区各部门贯彻落实中央全面深化改革委员会会议精神深入推进改革督察工作的报告》。</p>\"},\"officeAuditId\":1,\"officeAuditName\":\"管理员\",\"unitAuditId\":59,\"unitAuditName\":\"李发山\",\"isTop\":0,\"isLoop\":0,\"rotationChart\":\"\",\"titlePicture\":\"0\",\"id\":3}";



	}
}
