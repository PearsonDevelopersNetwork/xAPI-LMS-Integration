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

import java.util.HashMap;

// Stores the session info issued to Activity Providers
public class SessionDatabase {
	
	// FOR DEMO PURPOSES ONLY
	// This should be replaced with a persistent data source
	// Session needs to expire and be purged
	private static final HashMap<String,String> db = new HashMap<String,String>();
	
	public static void save(String id, String token) {
		if(id == null || token == null || 
				id.trim().length() == 0 || token.trim().length()==0) {	
			throw new IllegalArgumentException("id and token are required");
		}
		
		db.put(id, token);
	}
	
	public static boolean verify(String id, String token) {
		
		if(id == null || token == null || 
				id.trim().length() == 0 || token.trim().length()==0) {	
			throw new IllegalArgumentException("id and token are required");
		}
		
		if(db.containsKey(id)) {
			return token.equals(db.get(id));
		}
		
		return false;
	}
}
