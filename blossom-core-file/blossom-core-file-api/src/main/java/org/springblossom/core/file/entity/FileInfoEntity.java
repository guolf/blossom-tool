package org.springblossom.core.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblossom.core.tool.jackson.serializer.LongJsonDeserializer;
import org.springblossom.core.tool.jackson.serializer.LongJsonSerializer;
import org.springblossom.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息实体
 *
 * @author guolf
 */
@Data
@TableName("t_file_info")
@JsonIgnoreProperties({
	"createUser",
	"createTime",
	"isDeleted"
})
public class FileInfoEntity implements Serializable {

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.ID_WORKER)
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long id;

	/**
	 * 文件名称
	 */
	@ApiModelProperty("文件名称")
	private String name;
	/**
	 * 文件相对路径
	 */
	@ApiModelProperty("文件相对路径")
	private String location;

	@ApiModelProperty("文件下载路径")
	private String url;

	/**
	 * 类型
	 */
	@ApiModelProperty("类型")
	private String type;
	/**
	 * md5校验值
	 */
	@ApiModelProperty("md5校验值")
	private String md5;
	/**
	 * 文件大小
	 */
	@ApiModelProperty("文件大小")
	private Long size;

	/**
	 * 分类
	 */
	@ApiModelProperty(value = "分类", notes = "图片：pic、文档：file、视频：video")
	private String classified;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Integer createUser;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 状态[0:未删除,1:删除]
	 */
	@TableLogic
	@ApiModelProperty(value = "是否已删除")
	private Integer isDeleted;
}
