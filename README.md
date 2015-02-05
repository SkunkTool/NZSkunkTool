Sterling Rapid UI Tool

Sterling OMS comes with a wealthy set of the APIs and services that system administrators, support personal, and business analysts often found indispensable. The access method frequently is the API tester - yfshttpapi/yantrahttpapitester.jsp page. The API tester is not user friendly and does not have the feature to be a general tool outside the development community.

I have developed the Sterling Rapid UI Tool - SkunkTool that provides the following capabilities:

* Invoke Sterling API and Services 
* Define user input form
* Save and reuse API input  and output
* Display the result in table format
* Output transformation
* Chaining API

Invoke Sterling API and Services 

The tool supports Sterling API invokation standard including the input, output template, and API security.

The tool can execute API and Services through Http Apitester Servlet – interop/InteropHttpServlet when the Sterling Web Application has DevMode enabled. 

The tool can also directly execute API and Service, in similar fashion as a Sterling agent.
 
Define user input form

For the support and business communities, the tool provides the ability to define a user input form with the look and feel of a regular business application. 

Here is an example of a user input form: 

<InputUI> 
  <Field Type="List" Name="EnterpriseCode" Label="Enterprise:" Default="Aurora">
      <Item Label="Aurora E Site" Value="Aurora" />
      <Item Label="BIGCO BD" Value="BIGCO_BD" />
      <Item Label="BIGCO" Value="BIGCO" />
   </Field>
  <Field Type="String" Size="24" Name="OrderNo" Label="Order Number:" />
   <Field Type="List" Name="DocumentType" Label="Document Type:" Default="0001">
      <Item Label="Sales Order" Value="0001" />
      <Item Label="Reture Order" Value="0003" />
   </Field>
   <Field Type="Boolean" Name="IgnoreCollectionDate" Label="Ignore Colleciton Date:" Default="true" />
   <Field Type="Boolean" Name="IgnoreTransactionDependencies" Label="Ignore Transaction Dep:" Default="true" />
</InputUI>
This would result in an UI for a support or business user to perform the process order payment task.


Save and reuse API input and output

The tool provides users' the ability to save and reuse input, output template, and result for an API. It automatically groups the assets by name of the API or service and type of the asset (input, output template, or result).




Display the result in table format

Not everyone is born to read xml straight up. The tool provides the ability to view API results in a table format so people can stop complain about their inability to read xml.


Output transformation

For operations post API response, the tool provides you the ability to perform xsl transformation on the result. Example are providing a single list of payments from multiple orders. 

Chaining API

Sometimes, we just cannot get enough of APIs. We want to call one API then use its output as input for another API then the output feeds into the third API and on and on. The tool allows connecting APIs and Services. For example you can get list of orders for a customer that are in hold, find their shipments, and cancel those shipments. 

