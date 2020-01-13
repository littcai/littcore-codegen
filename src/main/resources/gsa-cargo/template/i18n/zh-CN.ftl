export default {

<#list rootList as root>
    ${root.code}: {
    <#list root.subList as sub>
        ${sub.code}: {
        <#list sub.subList as sub2>
            ${sub2.code}: "${sub2.zhCN}"
        </#list>
        }
    </#list>
    }
</#list>

}