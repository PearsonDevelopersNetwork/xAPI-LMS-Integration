# Experience API (xAPI) LMS Integration

## App Overview

This sample application demonstrates how LMS customers might integrate with a third party application to support Experience API (xAPI) Activity Providers. A LMS launches the application through a [LTI launch](http://developers.imsglobal.org/) with a custom parameter containing the URL of an Activity Provider. The application transforms the learner's profile from the LMS into the xAPI format expected by the Activity Provider, and it connects the Activity Provider with a LRS using credentials specific to the launch. This connection is accomplished by a LRS proxy, which the application provides to the Activity Provider. This allows the application to give the Activity Provider unique credentials for each learner's session. The application manages and verifies these credentials before relaying xAPI request to the LRS. The Activity Provider acts exactly how it would when interacting directly with the LRS.

This application's intent is to explore methods of enabling LMS customers to reliably connect learners to xAPI capable Activity Providers. The use of LTI makes this portable to any LMS. The Activity Provider only needs to support the simplest authentication option (Basic Auth) for xAPI. Most importantly, the learner doesn't need any association with the LRS. Their identity has been verified by originating at the LMS.

### Scope of Functionality 

This sample application is intended for demonstration purposes, so we made it easy for you to test drive it. It doesn't require you to setup a database, because it is using an in-memory store for persistence. It can be configured quickly, because all configuration is stored unencrypted. All use cases have not been considered. You would not be able to deploy this in a production setting without making modifications. The good news is you can experience this application's potential quickly. Continue reading to learn how!

## Prerequisites

### Build Environment 

  * Apache Maven should be installed and configured.
  * Java 7 or greater is required.

### Server Environment 

  * This application assumes you're running an application server (i.e. Tomcat). 
  * This application requires Java 7 or greater.

## Installation

### Application Configuration

#### LTI Setup

  1. Your LMS provider configures a [LTI](http://developers.imsglobal.org/) for `http://yourserver.com/xLMS/sso` with a `custom_xapi_ap_url` parameter.
    * LearningStudio customers can follow the [Outbound SSO setup](http://developer.pearson.com/learningstudio/set) instructions.
  2. Instructors add LTI launch links to their courses.
    * LearningStudio customers can follow the [Using SSO Links](http://developer.pearson.com/learningstudio/integrating-links-learningstudio) instructions.
  
Note: The value in the `custom_xapi_ap_url` parameter must be URL encoded twice in the launch URL.
 
#### Application Setup

The 3rd party ProxyServlet accepted the xAPI endpoint in the web.xml, so all config parameters were kept there for consistency. Replace the `{placeHolder}` values in the init parameters of the following servlets and filters.

src/main/webapp/WEB-INF/web.xml:

| Object Name | Param Name | Placeholder | Description |
| ----------- | ---------- | ----------- | ----------- |
| XapiAuthFilter | targetBasicAuth | `{lrs-basic-auth}` | Base64 encoded LRS credentials |
| XapiProxyServlet | targetUri | `{lrs-endpoint}` | Endpoint URL for LRS |
| SSOServlet | ltiConsumerKey | `{lti-consumer-key}` | Key from LTI credentials |
| SSOServlet | ltiSharedSecret | `{lti-shared-secret}` | Secret from LTI credentials |    

### Server Deployment

#### Build

Install [IMSGlobal's basiclti-util-java](https://github.com/IMSGlobal/basiclti-util-java/) locally by running the following outside this application's directory:

~~~~~~~~~~~~~~
git clone https://github.com/IMSGlobal/basiclti-util-java.git
cd basiclti-util-java/
mvn clean install
~~~~~~~~~~~~~~

Run `mvn clean package` from this application's directory to compile and assemble a war file.

#### Server 

Simply copy the `target/xLMS.war` file to your server. You should be able to access the application's LTI entry point from an address like: 

`http://yourserver.com/xLMS/sso`

Note: A 405 error will be returned when accessing this URL in a browser. Only a POST operation is supported.

## License

Copyright (c) 2015 Pearson Education, Inc.
Created by Pearson Developer Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
