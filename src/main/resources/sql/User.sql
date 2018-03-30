
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

		
		
		