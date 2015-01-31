<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--
 * Copyright 2006 Dr. Georg Fischer <punctum at punctum dot kom>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>

<html xmlns="http://www.w3.org/1999/xhtml">
<!-- 
    output some numbered system message
    @(#) $Id: message.jsp 37 2008-09-08 06:11:04Z gfis $
    Copyright (c) 2005 Dr. Georg Fischer <punctum@punctum.com>
    2005-08-18
-->
<%
final String [] msgText = new String [] 
    { /* 000 */ "unspecified system error"
    , /* 001 */ "invalid function"
    , /* 002 */ "invalid message number"
    , /* 003 */ "invalid language code"
    } ;
%>
<head>
    <title>checkdig message</title>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
</head>

<body>
<%
    String msg      = (String) session.getAttribute("messno");
    String function = (String) session.getAttribute("function");
    int msgNo = 000;
    try {
        msgNo = Integer.parseInt(msg);
        if (msgNo < 1 || msgNo >= msgText.length) {
            msgNo = 002;
        }
    } catch (Exception exc) {
    }
    
%>
<p>
Message No. <%= msg %>
</p>
<h2><%= msgText[msgNo] %></h2>
<h2>function= ?<%=function%>?</h2>
</body>
</html>
