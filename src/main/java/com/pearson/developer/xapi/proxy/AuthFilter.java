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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.codec.binary.Base64;

// Validates auth from Activity Provider before replacing with auth for LRS
public class AuthFilter implements Filter {
	private FilterConfig config;
	
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		
		// AJAX will preflight xAPI request with OPTIONS request without Authorization header
		if("OPTIONS".equals(req.getMethod())) {
			chain.doFilter(request, response);
			return;
		}
		
		boolean authorized = false;
		try { // decode and verify the basic auth credentials
			String authHeader = req.getHeader("Authorization");
			authHeader = authHeader.substring("Basic ".length());
			String decodedAuthHeader = new String(Base64.decodeBase64(authHeader),"UTF-8");
			String[] credentials = decodedAuthHeader.split(":");
			if(credentials.length == 2) {
				String username = credentials[0];
				String password = credentials[1];
				authorized = SessionDatabase.verify(username, password);
			}
		}
		catch(Exception e) {
			// do nothing
		}
		
		// proceed to xAPI if session was authorized
		if(authorized) {			
			final String targetBasicAuth = config.getInitParameter("targetBasicAuth");
			
			// need to give the LRS it's expected Authorization value
			HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req) {
		        @Override
		        public String getHeader(String name) {
		            if("Authorization".equalsIgnoreCase(name)) {
		            	return targetBasicAuth;
		            }
		            return super.getHeader(name);
		        }
		        
		        @Override
		        public Enumeration<String> getHeaders(String name) {
		            if("Authorization".equalsIgnoreCase(name)) {
		            	List<String> values = new ArrayList<String>();
		            	values.add(targetBasicAuth);
		            	return Collections.enumeration(values);
		            }
		            return super.getHeaders(name);
		        }
		    };
			chain.doFilter(requestWrapper, response);
			return;
		}		
		
		// respond with a 401 if missing auth
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.reset();
		resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		resp.getWriter().println("401 - Unauthorized");
	}

	public void destroy() {
		this.config = null;
	}
}
