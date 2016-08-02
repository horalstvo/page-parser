#### Page parser

Sanbox web app based on SpringBoot with Thymeleaf and Spring MVC. It was whipped up under three hours so my apologies for sharp edges.

It is packaged as WAR and needs to be deployed on Tomcat. For standalone just mark `spring-boot-starter-tomcat` as `provided` and add a `main` method to `App.java`.

The only web page is located at **/parse**

You can fill in URL and submit it. It will return some information about the web page: 
* number of internal links
* number of external links 
* number of inaccessible links
* page title
* headings counts
* HTML version

In case of error it returns the error message from the exception and HTTP status code if possible.

One can see there also an example on how to test a Spring MVC controler.
