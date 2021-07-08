<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
<title>Request Status</title>
</head>
<body>
<div class="jumbotron" style="border-radius: 32px ;opacity: 85%; padding-top: 5% ; padding-bottom: 100%; padding-left: 35%; padding-right: 20%;">
 <div class="row">
 <div class="col-md">
<p>${request}</p>
<p>${date}</p>
<p>${charge}</p>
<p>${prCharge}</p>
<p>${errmsg}</p>
<a href="cancelrequest/${id}"><button class="btn btn-primary">Cancel-Request</button></a>
</div>
</div>
</div>
</body>
</html>