<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/portal/css/bootstrap.min.css">
        <link href="https://fonts.googleapis.com/css?family=Oswald:400" rel="stylesheet">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
            integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@200&display=swap" rel="stylesheet">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" 
            integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
        <script src="/portal/js/bootstrap.min.js"></script>
<title>Track Status</title>
</head>
<body>
<div class="jumbotron" style="border-radius: 32px ;opacity: 85%; padding-top: 5% ; padding-bottom: 100%; padding-left: 35%; padding-right: 20%;">
<form:form method="get" action="orderrequest/${requestId}"> 
 <div class="row">
 <div class="col-md-6 ">
<label for="requestId">Request-Id</label>
<input type="text" class="form-control" id="requestId"placeholder="Enter the Request Id" name= "requestId" required>
</div>
</div>
<br>
<button type="submit"  class="btn btn-primary">Search</button><a href="order">  <button class="btn btn-primary">Home-Page</button></a>
</form:form>
<br>
<img alt="searching" src="https://media.tenor.com/images/3ce8820ca4465328f4aa59674073b18b/tenor.gif">

</div>
</body>
</html>