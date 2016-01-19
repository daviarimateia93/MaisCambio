- JavaCodeStyleFormatter.xml			(import this java code style)
- JavaScriptCodeStyleFormatter.xml		(import this javascript code style)

HOW TO SET UP
- This project requires Java 8
- Handling special chars in parameter values
	
	- Tomcat: add URIEncoding attribute to <Connector> element in /conf/server.xml:
	  <Connector ... URIEncoding="UTF-8">
	  
	- Glassfish: add <parameter-encoding> to /WEB-INF/glassfish-web.xml (or sun-web.xml for older versions):
	  <parameter-encoding default-charset="UTF-8" />
	  