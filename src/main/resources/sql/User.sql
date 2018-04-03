
--start: insert: insertUserBatch;
insert user (name, address, age, user_account) values ('${name}', '${address}'
,${age}, '${userAccount}'
)

--end: insert: insertUserBatch


--start:select: queryForUser; resultType=com.six.mydb.entity.User;
select * from user


--end:select:queryForUser



--start:select:queryAdmin; paramType=java.util.HashMap; resultType=java.util.HashMap;
		SELECT * FROM user WHERE 1 = 1  
		<#if type==4>
		 and name='${name}'
		</#if>
		
		
--end:select:queryAdmin

--start:select:queryAdmin2
		SELECT
			name
		FROM
			admin_menu
		WHERE
			1 = 1
		<#if type==4>
		 and name='查看'
		</#if>
		
		
		
--end:select:queryAdmin2

		
--start:insert: insertUser;
INSERT INTO `test2`.`user` (`name`, `age`, `address`) VALUES ('aaa', 12, 'sh');
		
		
--end:insert: insertUser
		

--start:insert: insertUserByParam;
INSERT INTO `test2`.`user` 
(
<#if name?? && name!=''>
name,
</#if>
age,user_account

)
 VALUES (
 <#if name?? && name!=''>
'${name}',
</#if>
${age}, '${userAccount}'

);
		
		
--end:insert: insertUserByParam

--start:update: updateUserByParam;
update test2.user
set
<#if name?? && name!=''>
name='${name}',
</#if> age=${age}, user_account='${userAccount}'
where 1=1
and id=${id}


--end:update: updateUserByParam

--start:select:selectByFor;
select * from user
where 1=1
<#if idList?size != 0>
and id in (
<#list idList as emp> 
    ${emp}     <#if emp_has_next>,</#if>
</#list>
)
</#if> 

--end:select:selectByFor


--start:delete:deleteUserByID;
delete from user where id=#{_param}


--end:delete:deleteUserByID

		
		
		