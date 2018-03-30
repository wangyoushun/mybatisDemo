
--start:select:queryAdmin
		SELECT
			*
		FROM
			admin_menu
		WHERE
			1 = 1
		<#if type==4>
		 and name='查看'
		</#if>
		
		
		AND menuid > 27
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
