<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%
	HttpServletRequest myrequest = request;
	String uriResourcesRoot = myrequest.getScheme() + "://" + myrequest.getServerName() + ":" + myrequest.getServerPort() + myrequest.getContextPath() + "/resources/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>SOA: Simple Image Server</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<!-- <link href="../../assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet"> -->

<!-- Custom styles for this template -->
<link href="css/starter-template.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Simple Image Server</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Home</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<div class="container starter-template">
		<h1>Middleware Technologies for Distributed Systems Project</h1>
		<h2>SOA: Simple Image Server</h2>

		<p>The goal of this project is to create an Image Server. The
			Image Server should provide a REST API for:</p>

		<div class="list-group">
			<a href="#new-user" class="list-group-item">Creating new users(each user should have a unique id and a name)</a> 
			<a href="#list-user" class="list-group-item">Listing the users in the system</a> 
			<a href="#new-image" class="list-group-item">Uploading .jpg images to a user's online storage space (each image should have a unique key and a title)</a> 
			<a href="#list-image" class="list-group-item">Listing the available images by user id</a> 
			<a href="#get-image" class="list-group-item">Obtaining the images from the server</a>
		</div>

		<p>The API should be developed in such a way as to support
			Hypermedia. Furthermore, the Image Server's REST API should also
			support <a href="#oAuth">OAuth</a> to allow third party applications to access a user's
			images.</p>
		
	</div><!-- /.container -->
	<p></p>
	<div class="container">
	
		<div class="panel panel-default" id="new-user">
			<div class="panel-heading">
				<h3 class="panel-title">Creating new users</h3>
			</div>
			<div class="panel-body">
				<dl class="dl-horizontal">
  					<dt>First Name:</dt><dd>Luke</dd>
  					<dt>Last Name:</dt><dd>Skywalker</dd>
  					<dt>Username:</dt><dd>jedi</dd>
  					<dt>Password:</dt><dd>yoda</dd>
				</dl>	
				<p>JSON: {"newuser":{"username":"jedi","password":"yoda","firstname":"Luke","lastname":"Skywalker"}}</p>
				<p><small><strong>NB: NO Auth required</strong></small></p>
				<pre>curl -k -i -XPOST -H "Content-type:application/json" -d '{"newuser":{"username":"jedi","password":"yoda","firstname":"Luke","lastname":"Skywalker"}}' <%=uriResourcesRoot.toString()+"users"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="list-user">
			<div class="panel-heading">
				<h3 class="panel-title">Listing the users in the system</h3>
			</div>
			<div class="panel-body">
				<p>@return a JSONArray containing all registered users</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -H "Accept: application/json" <%=uriResourcesRoot.toString()+"users"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="new-image">
			<div class="panel-heading">
				<h3 class="panel-title">Uploading .jpg images</h3>
			</div>
			<div class="panel-body">
				<p>Upload an image to the server</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -i -X POST -H "Content-Type: multipart/form-data" -F file=@test.jpg <%=uriResourcesRoot.toString()+"images"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="list-image">
			<div class="panel-heading">
				<h3 class="panel-title">Listing the available images by user id</h3>
			</div>
			<div class="panel-body">
				<p>@return a JSONArray containing all the images ordered by user id</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -H "Accept: application/json" <%=uriResourcesRoot.toString()+"images"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="get-image">
			<div class="panel-heading">
				<h3 class="panel-title">Obtaining the images from the server</h3>
			</div>
			<div class="panel-body">
				<p>@return the image (binary array)</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -o "image.jpg" -k --basic -u "hansolo:polpo" -H "Accept: image/jpeg" <%=uriResourcesRoot.toString()+"images/2"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
	</div><!-- /.container -->
	
	<div class="container" id="oAuth">
		<h2> OAuth2</h2>
		<p> spiegare qualcosa </p>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Request an Authorisation Token</h3>
			</div>
			<div class="panel-body">
			<p>Parameters:</p>
				<dl class="dl-horizontal">
					<dt>client_id:</dt><dd>1</dd>
					<dt>scope:</dt><dd></dd>
					<dt>state:</dt><dd>4330544</dd>
					<dt>redirect_uri:</dt><dd>https://192.168.1.100:8181/imageserver/resources/printParam</dd>
					<dt>response_type:</dt><dd>code</dd>
				</dl>
				<p>@return the authorisation code)</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: application/json" -L "<%=uriResourcesRoot.toString()+"authz?client_id=1&scope=&state=4330544&redirect_uri=" + uriResourcesRoot.toString()+ "printParam" +"&response_type=code"%>" &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Trade the Authorisation Token for an Access Token</h3>
			</div>
			<div class="panel-body">
			<p>Parameters:</p>
				<dl class="dl-horizontal">
					<dt>client_id:</dt><dd>1</dd>
					<dt>client_secret:</dt><dd>taaj6o1t4d1ennm2cfqb2sqlql</dd>
					<dt>redirect_uri:</dt><dd>https://192.168.1.100:8181/imageserver/resources/printParam</dd>
					<dt>grant_type:</dt><dd>authorization_code</dd>
					<dt>code:</dt><dd>AUTHZ_TOKEN</dd>
				</dl>
				<pre>curl -k -X POST -H "Accept: application/json" -H "Authorization: Bearer AUTHZ_TOKEN"  -H "Content-Type: application/x-www-form-urlencoded" -L "<%=uriResourcesRoot.toString()+"token?client_id=1&client_secret=taaj6o1t4d1ennm2cfqb2sqlql&redirect_uri=" + uriResourcesRoot.toString()+ "printParam" +"&grant_type=authorization_code&code=AUTHZ_TOKEN"%>" &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Get a resource using the Access Token</h3>
			</div>
			<div class="panel-body">
				<pre>curl -k -H "Authorization: Bearer ACCESS_TOKEN" -H "Accept: application/json" <%=uriResourcesRoot.toString()+"images"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
	</div><!-- /.container -->
	
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
	<script src="js/bootstrap.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<!-- <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script> -->
</body>
</html>