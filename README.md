# Description

A simple BurpSuite extension that allows URL remappings of proxy traffic using Regex expressions. BurpSuite only supports redirection of proxy connections to a single endpoint and maintains the protocol, regardless of the endpoint's port. The extension aims to allow more flexible redirection rules, including upgrading and downgrading of the protocol or blackholing of specific requests.

# Requirements

* BurpSuite (Free or Professional)
* Java 8 or newer

# Build and Installation

The plugin was developed using Netbeans and relies on Netbeans' GUI designer. Simply open the project in Netbeans and run build; a jar file should then be created in the project's dist directory.

Load the extension's jarfile using BurpSuite's Extender tab.

# BAppStore

TODO: Will hopefully publish the plugin on extension store for BurpSuite.

# Screenshots

## Main Tab

![main_tab](/uploads/dbdbae51630f81ec76e186ca222d0adc/main_tab.png)

## Rule Editor
![edit](/uploads/21574c501f32f93a592702d966831cd7/edit.png)

## Redirected Request
![redirected_request](/uploads/5ae8b323814eb745ca85a87e5867a3f3/redirected_request.png)

# Latest Release 

[burp-redirector.jar](/uploads/469979b59fb2c4a1672076d5a4cbf5f1/burp-redirector.jar)