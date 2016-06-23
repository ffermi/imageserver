<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%
	HttpServletRequest myrequest = request;
	String root = myrequest.getScheme() + "://" + myrequest.getServerName() + ":" + myrequest.getServerPort() + myrequest.getContextPath() + "/resources/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	<title>SOA: Simple Image Server</title>
	<!-- Bootstrap core CSS -->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<!-- Custom styles for this template -->
	<link href="css/starter-template.css" rel="stylesheet">
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
		<h1>Middleware Technologies for Distributed Systems</h1>
		<h2>SOA: Simple Image Server</h2>
		<p></p>
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
				<table class="table table-striped">
	  				<thead>
	  					<tr><th>key</th><th>value</th></tr>
	  				</thead>
	  				<tbody>
	  					<tr><td>firstname</td><td>Luke</td></tr>
	  					<tr><td>lastname</td><td>Skywalker</td></tr>
	  					<tr><td>username</td><td>jedi</td></tr>
	  					<tr><td>password</td><td>yoda</td></tr>
	  				</tbody>
				</table>				
				<p><small><strong>NB: NO Auth required</strong></small></p>
				<pre>curl -k -i -XPOST -H "Content-type:application/json" -d '{"newuser":{"username":"jedi","password":"yoda","firstname":"Luke","lastname":"Skywalker"}}' <%=root+"users"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="list-user">
			<div class="panel-heading">
				<h3 class="panel-title">Listing the users in the system</h3>
			</div>
			<div class="panel-body">
				<p>return a JSONArray containing all registered users</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -H "Accept: application/json" <%=root+"users"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="new-image">
			<div class="panel-heading">
				<h3 class="panel-title">Uploading .jpg images</h3>
			</div>
			<div class="panel-body">
				<p>Upload an image to the server</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -i -X POST -H "Content-Type: multipart/form-data" -F file=@test.jpg <%=root+"images"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="list-image">
			<div class="panel-heading">
				<h3 class="panel-title">Listing the available images by user id</h3>
			</div>
			<div class="panel-body">
				<p>return a JSONArray containing all the images ordered by user id</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -H "Accept: application/json" <%=root+"images"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default" id="get-image">
			<div class="panel-heading">
				<h3 class="panel-title">Obtaining the images from the server</h3>
			</div>
			<div class="panel-body">
				<p>return the image (binary array)</p>
				<p><small><strong>NB: Basic Auth or Bearer Token REQUIRED</strong></small></p>
				<pre>curl -o "image.jpg" -k --basic -u "hansolo:polpo" -H "Accept: image/jpeg" <%=root+"images/2"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
	</div><!-- /.container -->
	
	<div class="container" id="oAuth">
		<h2> OAuth2</h2>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Request an Authorisation Token</h3>
			</div>
			<div class="panel-body">
				<p>Parameters:</p>
				<table class="table table-striped">
	  				<thead>
	  					<tr><th>key</th><th>value</th></tr>
	  				</thead>
	  				<tbody>
	  					<tr><td>client_id</td><td>gcczxnqpjkc65grfx2wewkmsegkhfvez</td></tr>
	  					<tr><td>scope</td><td></td></tr>
	  					<tr><td>state</td><td>4330544</td></tr>
	  					<tr><td>redirect_uri</td><td><%=root+"printParam"%></td></tr>
	  					<tr><td>response_type</td><td>code</td></tr>
	  				</tbody>
				</table>				
				<p><small><strong>NB: Basic Auth REQUIRED</strong></small></p>
				<pre>curl -k --basic -u "hansolo:polpo" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: application/json" -L "<%=root+"authz?client_id=1&scope=&state=4330544&redirect_uri=" + root+ "printParam" +"&response_type=code"%>" &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Trade the Authorisation Token for an Access Token</h3>
			</div>
			<div class="panel-body">
			<p>Parameters:</p>
				<table class="table table-striped">
	  				<thead>
	  					<tr><th>key</th><th>value</th></tr>
	  				</thead>
	  				<tbody>
	  					<tr><td>client_id</td><td>gcczxnqpjkc65grfx2wewkmsegkhfvez</td></tr>
	  					<tr><td>client_secret</td><td>gnug8962t4fssqrybmbjxgqb9x3fursm</td></tr>
	  					<tr><td>redirect_uri</td><td><%=root+"printParam"%></td></tr>
	  					<tr><td>grant_type</td><td>authorization_code</td></tr>
	  					<tr><td>code</td><td>MY_AUTHORIZATION_CODE</td></tr>
	  				</tbody>
				</table>	
				<pre>curl -k -X POST -H "Accept: application/json" -H "Content-Type: application/x-www-form-urlencoded" -L "<%=root+"token?client_id=1&client_secret=taaj6o1t4d1ennm2cfqb2sqlql&redirect_uri=" + root+ "printParam" +"&grant_type=authorization_code&code=MY_AUTHORIZATION_CODE"%>" &amp;&amp; echo</pre>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Get a resource using the Access Token</h3>
			</div>
			<div class="panel-body">
				<p>Use <i>Bearer</i> authorization </p>
				<pre>curl -k -H "Authorization: Bearer MY_ACCESS_TOKEN" -H "Accept: application/json" <%=root+"images"%> &amp;&amp; echo</pre>
			</div>
		</div>
		
	</div><!-- /.container -->
	
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
	<script src="js/bootstrap.min.js"></script>
</body>
</html>