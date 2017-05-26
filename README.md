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

![main_tab](http://imgur.com/ERjf6UU)

## Rule Editor
![edit](http://imgur.com/sRJUaXj)

## Redirected Request
![redirected_request](http://imgur.com/JS30jN1)

