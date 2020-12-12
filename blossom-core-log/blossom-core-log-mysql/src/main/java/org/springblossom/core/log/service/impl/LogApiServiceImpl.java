/**
 * Copyright (c) 2018-2028, 上海车嗨网络科技有限公司江西分公司 .
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblossom.core.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblossom.core.log.mapper.LogApiMapper;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.service.ILogApiService;
import org.springframework.stereotype.Service;


/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class LogApiServiceImpl extends ServiceImpl<LogApiMapper, LogApi> implements ILogApiService {

}
