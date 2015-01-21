<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xalan="http://xml.apache.org/xslt">
	<xsl:output method="xml" indent="yes" xalan:indent-amount="2" />
	<xsl:template match="/XXXList">
		<MultiApi>
			<xsl:for-each select="XXX">
				<API FlowName="" IsExtendedDbApi="" Name="" Version="">
					<Input>
						<YYY>
							<xsl:attribute name="ZZZ">
								<xsl:value-of select="@UUUU" />
							</xsl:attribute>
						</YYY>
					</Input>
				</API>
			</xsl:for-each>
		</MultiApi>
	</xsl:template>
</xsl:stylesheet>