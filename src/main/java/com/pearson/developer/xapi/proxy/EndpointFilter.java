/*
* Experience API (xAPI) LMS Integration
*
* Need Help or Have Questions?
* Please use the PDN Developer Community at https://community.pdn.pearson.com
*
* @category   LearningStudio Sample Application - xAPI-LMS-Integration
* @author     Wes Williams <wes.williams@pearson.com>
* @author     Pearson Developer Services Team <apisupport@pearson.com>
* @copyright  2015 Pearson Education, Inc.
* @license    http://www.apache.org/licenses/LICENSE-2.0  Apache 2.0
* @version    1.0
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.pearson.developer.xapi.proxy;

import javax.servlet.*;
import javax.servlet.http.*;

// Validates request are for xAPI endpoints
public class EndpointFilter implements Filter {
	
	public void init(FilterConfig config) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		
		String path = req.getRequestURI().substring(req.getContextPath().length() + req.getServletPath().length());
		
		// only allow non-oauth endpoints listed at:
		//   https://github.com/adlnet/xAPI-Spec/blob/1.0.1/xAPI.md#appendix-h-table-of-all-endpoints
		if("/statements".equals(path) || 
				"/agents".equals(path) || 
				"/agents/profile".equals(path) || 
				"/activities".equals(path) || 
				"/activities/state".equals(path) || 
				"/activities/profile".equals(path) || 
				"/about".equals(path)) {
			
			chain.doFilter(request, response);
			return;
		}
		
		// respond with a 404 if endpoint unexpected
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.reset();
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		resp.getWriter().println("404 - Not Found");
	}

	public void destroy() {

	}
}