<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/30/2023
  Time: 6:03 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Reviews</title>
    <link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<div class="container mt-5">
    <div class="panel-primary">
        <div class="panel-heading">
            <h1 class="panel-title">COMMENT BOX USING AJAX THROUGH JSP</h1>
        </div>
        <div class="panel-body">
            <div class="form-group col-md-4">
                <label>Name</label>
                <input class ="form-control" type="text" id="username">
            </div>
            <div class="clearfix"></div>
            <div class="form-group col-md-4">
                <label>Review</label>
                <textarea class="form-control" rows="8" id="comment" required="required"></textarea>
            </div>
            <div class="clearfix"></div>
            <div class="form-group col-md-6">
                <button class="btn btn-primary" type="button" onclick="loadAjax()">POST</button>
            </div>
        </div>
    </div>
    <p id="print"></p>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.js"></script>
<script type="text/javascript">

    function loadAjax(){
        var username= document.getElementById("username").value;
        var description= document.getElementById("description").value;
        if(username.trim() =="" || description.trim()==""){
            alert("All fields are Required");
            return false;
        }


        var url="ajaxrequestPage.jsp?username="+username +"&description="+description;



        if(window.XMLHttpRequest){

            request = new XMLHttpRequest();

        }else if(window.ActiveXObject){

            request = new ActiveXObject("Microsoft.XMLHTTP");
        }
        try{
            request.onreadystatechange=sendInfo;
            request.open("POST",url,true);
            request.send();

        }catch(e){
            document.write(e);
        }
    }

    function sendInfo(){
        var p =		document.getElementById("print");

        if(request.readyState ==1){
            var text = request.responseText;
            p.innerHTML="Please Wait...";
            console.log("1");
        }

        }
        if(request.readyState ==2){
            var text = request.responseText;
            p.innerHTML=" Your Review has been Posted  "+text;
        }
    }
</script>
</body>
</html>

