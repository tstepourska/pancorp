<#--
tailored version of the simple template's checkboxlist.ftl

this version follows the WET practice of surrounding the checkbox input
with the label, which permits the form-checkbox (and optionally form-label-inline)
css classes to be applied properly.
-->
<#assign itemCount = 0/>
<#if parameters.list??>
<@s.iterator value="parameters.list">
    <#assign itemCount = itemCount + 1/>
    <#if parameters.listKey??>
        <#assign itemKey = stack.findValue(parameters.listKey)/>
        <#assign itemKeyStr = stack.findString(parameters.listKey)/>
    <#else>
        <#assign itemKey = stack.findValue('top')/>
        <#assign itemKeyStr = stack.findString('top')>
    </#if>
    <#if parameters.listLabelKey??>
    <#-- checks the valueStack for the 'valueKey.' The valueKey is then looked-up in the locale 
       file for it's localized value.  This is then used as a label -->
        <#assign itemValue = struts.getText(stack.findString(parameters.listLabelKey))/>
    <#elseif parameters.listValue??>
        <#assign itemValue = stack.findString(parameters.listValue)!""/>
    <#else>
         <#assign itemValue = stack.findString('top')/>
    </#if>
    <#if parameters.listCssClass??>
        <#if stack.findString(parameters.listCssClass)??>
          <#assign itemCssClass= stack.findString(parameters.listCssClass)/>
        <#else>
          <#assign itemCssClass = ''/>
        </#if>
    </#if>
    <#if parameters.listCssStyle??>
        <#if stack.findString(parameters.listCssStyle)??>
          <#assign itemCssStyle= stack.findString(parameters.listCssStyle)/>
        <#else>
          <#assign itemCssStyle = ''/>
        </#if>
    </#if>
    <#if parameters.listTitle??>
        <#if stack.findString(parameters.listTitle)??>
          <#assign itemTitle= stack.findString(parameters.listTitle)/>
        <#else>
          <#assign itemTitle = ''/>
        </#if>
    </#if>
<div class="checkbox">
<label<#rt/> 
    <#if parameters.id?has_content>
        for="${parameters.id?html}-${itemCount}"<#rt/>
    <#else>
        for="${parameters.name?html}-${itemCount}"<#rt/>
    </#if>
    <#if itemCssClass! != "">
     class="${itemCssClass?html}"<#rt/>
    <#else>
        <#if parameters.cssClass?has_content>
     class="${parameters.cssClass?html}"<#rt/>
        </#if>
    </#if>
>    
<input type="checkbox" name="${parameters.name?html}" value="${itemKeyStr?html}"<#rt/>
    <#if parameters.id?has_content>
       id="${parameters.id?html}-${itemCount}"<#rt/>
    <#else>
       id="${parameters.name?html}-${itemCount}"<#rt/>
    </#if>
    <#if tag.contains(parameters.nameValue, itemKey)>
       checked="checked"<#rt/>
    </#if>
    <#if parameters.disabled!false>
       disabled="disabled"<#rt/>
    </#if>
    <#if itemCssStyle! != "">
     style="${itemCssStyle?html}"<#rt/>
    <#else>
        <#if parameters.cssStyle?has_content>
     style="${parameters.cssStyle?html}"<#rt/>
        </#if>
    </#if>
    <#if itemTitle! != "">
     title="${itemTitle?html}"<#rt/>
    <#else>
        <#if parameters.title?has_content>
     title="${parameters.title?html}"<#rt/>
        </#if>
    </#if>
    <#include "/${parameters.templateDir}/simple/css.ftl" />
    <#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
    <#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
    <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
        />
${itemValue?html}</label>
</div>
</@s.iterator>
    <#else>
    &nbsp;
</#if>
<input type="hidden" id="__multiselect_${parameters.id?html}" name="__multiselect_${parameters.name?html}"
       value=""<#rt/>
<#if parameters.disabled!false>
       disabled="disabled"<#rt/>
</#if>
 />
