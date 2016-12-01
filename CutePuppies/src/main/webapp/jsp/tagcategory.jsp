<%-- 
    Document   : tagcategory
    Created on : Nov 30, 2016, 7:24:34 PM
    Author     : apprentice
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SWG Cute Puppy Adoption Center Dashboard</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/TagsCategoriesCSS.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=McLaren" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/navbar-fixed-side.css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-3 col-lg-2">
                    <nav class="navbar navbar-default navbar-fixed-side">
                        <div class="container-fluid">
                            <div class="navbar-header">
                                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                                    <span class="sr-only">Toggle navigation</span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                </button>
                                <a class="navbar-brand" href="${pageContext.request.contextPath}/">SWG Cute Puppies</a>
                            </div>
                            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                                <ul class="nav navbar-nav">
                                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Posts</a></li>
                                    <li><a href="#">Pages</a></li>
                                    <li><a href="#">Users</a></li>
                                    <li><a href="${pageContext.request.contextPath}/admin/manageCategories">Categories</a></li>
                                    <li><a href="${pageContext.request.contextPath}/admin/manageTags">Tags</a></li>
                                </ul>
                            </div>
                        </div>
                    </nav>
                </div>
                <div class="col-sm-9 col-lg-10">
                    <div class="col-md-4 col-lg-3 col-sm-6">
                    <table id="editTable" class="table table-striped">
                        <caption id="tagOrCategory" class="text-center">${categoryTag}</caption>
                        <tbody id="tableBody">

                        </tbody>
                    </table>
                    </div>
                </div>
                <footer>
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <p>Powered by Java and Bootstrap</p>
                        </div>
                    </div>
                </footer>

            </div>
        </div>
        <script>var contextPath = "${pageContext.request.contextPath}"</script>
        <script src="${pageContext.request.contextPath}/js/jquery-2.2.4.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/TagsCategoriesJS.js"></script>  
        <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
        <script>tinymce.init({selector: 'textarea'});</script>
    </body>
</html>
