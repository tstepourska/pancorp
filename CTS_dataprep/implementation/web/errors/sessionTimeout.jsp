<%-- 
inactivity and sessionalive time are set to 
30 minutes (the session-timeout value set in web.xml), 
minus 180 seconds (the reactionTime value for the dialog).

The value for sessionErrorPage is retrieved from jndi 
and stored as an application-scope attribute by the
WebAppMonitor context listener.   
--%>
<span class="wb-sessto" 
    data-wet-boew='{
        "inactivity": 1620000,
        "sessionalive": 1620000, 
        "reactionTime": 180000, 
        "logouturl": "<%=application.getAttribute("sessionErrorPage")%>", 
        "refreshCallbackUrl": "<%=request.getContextPath()%>/pub/rfrsh.action",
        "refreshOnClick": false
    }'>
</span>