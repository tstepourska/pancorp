<#--
tailored version of the simple template's fielderror.ftl

Because our errors are emitted inside a label, they
cannot include block-level elements.  The default fielderror
template emits error messages as a (block level) unordered list.
This version uses (inline) br tags to separate the messages
instead.
-->
<#if fieldErrors??><#t/>
    <#assign eKeys = fieldErrors.keySet()><#t/>
    <#assign eKeysSize = eKeys.size()><#t/>
    <#assign doneStartUlTag=false><#t/>
    <#assign doneEndUlTag=false><#t/>
    <#assign haveMatchedErrorField=false><#t/>
    <#if (fieldErrorFieldNames?size > 0) ><#t/>
        <#list fieldErrorFieldNames as fieldErrorFieldName><#t/>
            <#list eKeys as eKey><#t/>
                <#if (eKey = fieldErrorFieldName)><#t/>
                    <#assign haveMatchedErrorField=true><#t/>
                    <#assign eValue = fieldErrors[fieldErrorFieldName]><#t/>
                    <#if (haveMatchedErrorField && (!doneStartUlTag))><#t/>
                    <span<#rt/>
                        <#if parameters.id?has_content>
                                id="${parameters.id?html}"<#rt/>
                        </#if>
                        <#if parameters.cssClass?has_content>
                                class="${parameters.cssClass?html}"<#rt/>
                        </#if>
                        <#if parameters.cssStyle?has_content>
                                style="${parameters.cssStyle?html}"<#rt/>
                        </#if>
                            <#lt/>>
                        <#assign doneStartUlTag=true><#t/>
                    </#if><#t/>
                    <#list eValue as eEachValue><#t/>
                        <span class="strong error label label-danger label-error"><#if parameters.escape>${eEachValue!?html}<#else>${eEachValue!}</#if></span>
                    </#list><#t/>
                </#if><#t/>
            </#list><#t/>
        </#list><#t/>
        <#if (haveMatchedErrorField && (!doneEndUlTag))><#t/>
        </span>
            <#assign doneEndUlTag=true><#t/>
        </#if><#t/>
        <#else><#t/>
        <#if (eKeysSize > 0)><#t/>
        <span<#rt/>
            <#if parameters.cssClass?has_content>
                    class="${parameters.cssClass?html}"<#rt/>
            </#if>
            <#if parameters.cssStyle?has_content>
                    style="${parameters.cssStyle?html}"<#rt/>
            </#if>
                <#lt/>>
            <#list eKeys as eKey><#t/>
                <#assign eValue = fieldErrors[eKey]><#t/>
                <#list eValue as eEachValue><#t/>
                    <span class="strong error label label-danger label-error"><#if parameters.escape>${eEachValue!?html}<#else>${eEachValue!}</#if></span>
                </#list><#t/>
            </#list><#t/>
        </span>
        </#if><#t/>
    </#if><#t/>
</#if><#t/>
