
# Anypoint Template: Salesforce and Siebel Account Aggregation	

<!-- Header (start) -->

Aggregates accounts from Salesforce and Oracle Siebel into a CSV file. This basic pattern can be modified to collect from more or different sources and to produce formats other than CSV. 

![536ee900-7ec3-4bc6-a1bb-9de68dc0933e-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/536ee900-7ec3-4bc6-a1bb-9de68dc0933e-image.png)

This application is triggered by an HTTP call that can be used manually or programmatically. Accounts are sorted such that the accounts only in Salesforce appear first, followed by accounts only in Siebel, and lastly by accounts found in both systems. The custom sort or merge logic can be easily modified to present the data as needed. This template also serves as a  base for building APIs using the Anypoint Platform.

Aggregation templates can be easily extended to return a multitude of data in mobile friendly form to power your mobile initiatives by providing easily consumable data structures from otherwise complex backend systems.

![](https://www.youtube.com/embed/jnEOalevv-8?wmode=transparent)
[![YouTube Video](http://img.youtube.com/vi/jnEOalevv-8/0.jpg)](https://www.youtube.com/watch?v=jnEOalevv-8)

<!-- Header (end) -->

# License Agreement

This template is subject to the conditions of the [MuleSoft License Agreement](https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf "MuleSoft License Agreement"). Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 

# Use Case

<!-- Use Case (start) -->

Aggregates accounts from Salesforce and Oracle Siebel Business Objects Instances and compares them to see which accounts can only be found in one of the two and which accounts are in both instances. 

For practical purposes this template generates the results as a CSV report sent by email.

This template extracts data from two systems, aggregates the data, compares values of fields for the objects, and generates a report on the differences. 

This template gets two accounts, one from Salesforce and other from Oracle Siebel Business Objects instance. Then it compares by the name of the accounts, and generates a CSV file which shows accounts in Salesforce, accounts in Oracle Siebel Business Objects, and accounts in both Salesforce and Oracle Siebel Business Objects. The report is then emailed to a configured group of email addresses configured in the `mule.*.properties` file.

# Considerations

<!-- Default Considerations (start) -->

<!-- Default Considerations (end) -->

<!-- Considerations (start) -->

To make this template run, there are certain preconditions that must be considered. All of them deal with the preparations in both, that must be made for the template to run smoothly. Failing to do so can lead to unexpected behavior of the template.

## Salesforce Considerations

To get this template to work:

- Where can I check that the field configuration for my Salesforce instance is the right one? See: [Salesforce: Checking Field Accessibility for a Particular Field](https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US "Salesforce: Checking Field Accessibility for a Particular Field").
- Can I modify the Field Access Settings? How? See: [Salesforce: Modifying Field Access Settings](https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US "Salesforce: Modifying Field Access Settings").

### As a Data Source

If the user who configured the template for the source system does not have at least _read only_ permissions for the fields that are fetched, then an _InvalidFieldFault_ API fault displays.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault 
[ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='Account.Phone, Account.Rating, Account.RecordTypeId, 
Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are 
attempting to use a custom field, be sure to append the '__c' 
after the custom field name. Reference your WSDL or the 
describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```

## Oracle Siebel Considerations

This template uses date time or timestamp fields from Oracle Siebel to do comparisons and take further actions. While the template handles the time zone by sending all such fields in a neutral time zone, it cannot discover the time zone in which the Siebel instance is on. It's up to you to provide this information. See [Oracle's Setting Time Zone Preferences](http://docs.oracle.com/cd/B40099_02/books/Fundamentals/Fund_settingoptions3.html).

### As a Data Destination

To make the Siebel connector work smoothly you have to provide the correct version of the Siebel JAR file that works with your Siebel installation. [See these prerequisites|[https://docs.mulesoft.com/connectors/siebel-connector#prerequisites](https://docs.mulesoft.com/connectors/siebel-connector#prerequisites)].

# Run it!

Simple steps to get this template running.

<!-- Run it (end) -->

## Running On Premises

In this section we help you run this template on your computer.

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime

If you are new to Mule, download this software:

- [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
- [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.

<!-- Where to download (end) -->

### Importing a Template into Studio

In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.

<!-- Importing into Studio (end) -->

### Running on Studio

After you import your template into Anypoint Studio, follow these steps to run it:

1. Locate the properties file `mule.dev.properties`, in src/main/resources.
2. Complete all the properties required per the examples in the "Properties to Configure" section.
3. Right click the template project folder.
4. Hover your mouse over `Run as`.
5. Click `Mule Application (configure)`.
6. Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
7. Click `Run`.


<!-- Running on Studio (end) -->

### Running on Mule Standalone

Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`.

To trigger the use case, browse to the local HTTP endpoint with the port you configured in your file. If this is, for example `9090` then browse to: `http://localhost:9090/generatereport` and this causes the application to create a CSV report and send it to the emails you configured in the `mule.*.properties` file.

## Running on CloudHub

When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the mule.env value.

Once your app is all set up and started, if you choose as domain name `template-sfdc2sieb-account-aggregation` to trigger the use case you just need to browse to `http://template-sfdc2sieb-account-aggregation.cloudhub.io/generatereport` and report is sent to the emails configured.

### Deploying a Template in CloudHub

In Studio, right click your project name in Package Explorer and select Anypoint Platform > Deploy on CloudHub.

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure

To use this template, configure properties such as credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.

### Application Configuration

<!-- Application Configuration (start) -->

**HTTP Connector Configuration**

- http.port `9090` 

**Salesforce Connector Configuration**

- sfdc.username `bob.dylan@org`
- sfdc.password `DylanPassword123`
- sfdc.securityToken `avsfwCUl7apQs56Xq2AKi3X`

**Oracle Siebel Connector Configuration**

- sieb.user SADMIN
- sieb.password SADMIN
- sieb.server 192.168.10.8
- sieb.serverName SBA\_82
- sieb.objectManager EAIObjMgr\_enu
- sieb.port 2321

**SMTP Services Configuration**

- smtp.host smtp.gmail.com
- smtp.port 587
- smtp.user exampleuser@gmail.com
- smtp.password ExamplePassword456

**Email Details**

- mail.from exampleuser@gmail.com
- mail.to woody.guthrie@gmail.com
- mail.subject Accounts Report
- mail.body Please find attached your Accounts Report
- attachment.name accounts\_report.csv

# API Calls

<!-- API Calls (start) -->

Salesforce imposes limits on the number of API calls that can be made.

This template calls both systems just once so this is not something to worry about.

# Customize It!

This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

- config.xml
- businessLogic.xml
- endpoints.xml
- errorHandling.xml

<!-- Customize it (end) -->

## config.xml

<!-- Default Config XML (start) -->

This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.

<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml

<!-- Default Business Logic XML (start) -->

Functional aspect of the template is implemented in this XML file, directed by one flow responsible of conducting the aggregation of data, comparing records and finally formatting the output, in this case a report.

Using Scatter-Gather component we are querying the data in different systems. After that the aggregation is implemented in DataWeave 2 script using Transform component. Aggregated results are sorted by source of existence:

1. Accounts only in Salesforce
2. Accounts only in Siebel
3. Accounts in both Salesforce and Siebel

These are transformed to CSV format. The final report in CSV format is sent to email that you configured in the `mule.*.properties` file.

<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml

<!-- Default Endpoints XML (start) -->

This is the file where you find the inbound and outbound sides of your integration app.

This template uses an HTTP Listener to trigger the use case and an SMTP transport to send the report.

### Trigger Flow

**HTTP Connector** - Start Report Generation:

- `${http.port}` is set as a property to be defined either on a property file or in CloudHub environment variables.
- The path configured by default is `generatereport` and you are free to change it to one you prefer.
- The host name for all endpoints in your CloudHub configuration should be defined as `localhost`. CloudHub then routes requests from your application domain URL to the endpoint.

### Outbound Flow

**SMTP Outbound Endpoint** - Send Mail

+ Both SMTP Server configuration and the actual mail to be sent are defined in this endpoint.

+ This flow is going to be invoked from the flow that does all the functional work: _mainFlow_, the same that is invoked from the Inbound Flow upon triggering of the HTTP Connector.

<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml

<!-- Default Error Handling XML (start) -->

This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.

<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->

