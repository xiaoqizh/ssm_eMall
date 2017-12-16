<html>
<head>
    <title>学生信息</title>
</head>
  <body>
   学生信息:<br>
   学号:${student.id}&nbsp;&nbsp;&nbsp;姓名：${student.name}&nbsp;&nbsp;&nbsp;住址:${student.address}
   <table border="1">
       <tr>
           <th>学号</th>
           <th>姓名</th>
           <th>地址</th>
       </tr>
       <#--整体包装在list里面-->
      <#list students as stu >
      <#if stu_index % 2 ==0 >
      <tr bgcolor="blue">
      <#else >
      <tr bgcolor="#7fff00">
      </#if>
          <th>${stu.id}</th>
          <th>${stu.name}</th>
          <th>${stu.address}</th>
      </tr>
      </#list>
   </table> <br>
   当前时间：${date?date}
  <#-- ?date 取年月日 ?time 取具体时间 ?datetime 综上-->
   <#--?string(pattern) 是自定义格式化字符串-->
   当前时间：${date?string("yyyy/MM/dd HH:mm:ss")}
   <br>
  <#--null值的处理方式 感叹号的意思是如果val为null 那么就是感叹号后面的东西
    如果不是空的话 那就显示原值-->
  val的处理方式: ${val!"val的值为空"} <br>
  <#--也可进行判断-->
  <#if val ??>
    val值不为空
  <#else >
   val的值为空或丢失
  </#if>
  </body>
</html>