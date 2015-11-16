# Introduction

This installer is a Java application which reads a JSON formatted configuration file and performs a software installation using a wizard-style GUI. The build process produces a self-contained DMG file for OS X 

The user is able to select from a list of options to install options are then installed with a progress bar and labels showing the current step. After completion of installation, commands that were successful during the installation phase will have their associated clean-up commands executed.

If a step fails then, after clean-up commands are executed, the standard output and standard error results of the failed step are shown. A "copy to clipboard" button is provided to suggest the user includes the result in their support request.

## Complete working example

This is an installation script with a single option which creates two files. During the installation process, a temporary directory is created which is removed during the clean-up phase:

	{
	  "options" : [
	    {
	      "label" : "Example Product",
	
	      "commands" : [
	
	        {
	          "label" : "Creating temporary directory",
	          "command" : "mkdir temp",
	
	          "cleanup" : [
	            {
	              "label" : "Removing temporary directory",
	              "command" : "rmdir temp"
	            }
	          ]
	        },
	
	        {
	          "label" : "Creating the foo file",
	          "command" : "touch foo"
	        },
	
	        {
	          "label" : "Creating the bar file",
	          "command" : "touch bar"
	        }
	      ]
	    }
	  ]
	}

## How to build

### Prerequisites

* Java Development Kit (you need the [JDK](http://java.sun.com/ "Java Development Kit"), not just the JRE)
* Maven (available via [homebrew](http://brew.sh/ "Homebrew") on OS X)

### Steps

Clone the repository from GitHub with:

    git clone https://github.com/rti-capture/installer

Copy in a suitable JRE or JDK to the `installer/RTI-VIPS Installer.app/Contents/PlugIns` directory. A copy of the JRE/JDK will be embedded in the DMG file and the Java bootstrap process will find and use it to avoid the need to have Java pre-installed.   

Now use Maven to build, test and create a DMG file:

	cd installer
	mvn clean package

When the build is complete, the DMG file will be in the `target` folder.

## Features

### Options

The installer offers a list of options to the user to install. The items are marked to install by default, however the user can chose not to install any of the options.

The options are configured as follows:

	{
	  "options" : [
	
	    {
	      "label" : "First option",
	
	      "commands" : [
	
	        {
	          "command" : "sleep 2",
	          "label" : "Sleep for two seconds"
	        }
	      ]
	    },
	
	    {
	      "label" : "Second option",
	
	      "commands" : [
	
	        {
	          "command" : "sleep 2",
	          "label" : "Sleep for two seconds"
	        }
	      ]
	    },
	
	    {
	      "label" : "Third option",
	
	      "commands" : [
	
	        {
	          "command" : "sleep 2",
	          "label" : "Sleep for two seconds"
	        }
	      ]
	    }
	  ]
	}

### Welcome / Completion messages

The messages shown in the welcome and finish pages can be customised with HTML labels by using `introMessage` and `finishMessage` as follows:

	{
	  "introMessage" : "<html><center><h2>Welcome to the RTI-VIPS installer</h2></center><p>Use this installer to install the software required for the RTI-VIPS system. Click \"Next\" to choose the components to install.</p></html>",
	
	  "finishMessage" : "<html><center>Thank you for installing the software. You can now run the software by opening a terminal window and using the 'rticapture' command.</center></html>",
	
	  "options" : [
	
	    {
	      "label" : "Test",
	
	      "commands" : [
	
	        {
	          "command" : "sleep 2",
	          "label" : "Sleep for two seconds"
	        }
	      ]
	    },
	  ]
	}


### Dependencies

In the following example, the installation option has a dependency which must be satisfied before the option is installed. The dependencies are named so that multiple options can refer to the same dependency.

Each dependency needs a `checkCommand` which is executed to test if the dependency is already met. If it not, then the `installCommands` commands are executed. Note that the `checkCommand` will be executed more than once, so you shouldn't make system changes at this point. 

In this example, the dependency is satisfied if a particular file is present. On the first run, the foo file is created. On the second or subsequent runs, the dependency is met and so the installation commands for the dependency are not executed. 

	{
	  "dependencies" : {
	
	    "foo dependency" : {
	
	      "label" : "Foo",
	
	      "checkCommand" : {
	        "label" : "Checking for the foo file",
	        "command" : "ls foo.txt"
	      },
	
	      "installCommands" : [
	
	        {
	          "label" : "Installing foo file",
	          "command" : "sleep 2"
	        },
	
	        {
	          "command" : "touch foo.txt"
	        }
	      ]
	    }
	  },
	
	  "options" : [
	
	    {
	      "label" : "Sleep Test",
	
	      "dependencies" : [ "foo dependency" ],
	
	      "commands" : [
	
	        {
	          "command" : "sleep 2",
	          "label" : "Sleep for two seconds"
	        }
	      ]
	    }
	  ]
	}

### Clean-up

Each command can have clean-up commands associated with it. The clean-up commands are run at the end of the installation to deal with cleaning up any temporary state (e.g. removing build directories, unmounting disk images, etc.) 


Commands are executed in order. However, the clean-up commands are executed in *reverse* order. The following example demonstrates how this works in practice:

	{
	  "options" : [
	
	    {
	      "label" : "Clean-up test",
	
	      "commands" : [
	
	        {
	          "label" : "Make foo directory",
	          "command" : "mkdir foo",
	
	          "cleanup" : [
	            {
	              "command" : "rmdir foo"
	            }
	          ]
	        },
	
	        {
	          "label" : "Make bar directory",
	          "command" : "mkdir foo/bar",
	
	          "cleanup" : [
	            {
	              "command" : "rmdir foo/bar"
	            }
	          ]
	        },
	
	        {
	          "label" : "Create baz file",
	          "command" : "touch foo/bar/baz"
	        },
	
	        {
	          "label" : "Remove baz file",
	          "command" : "rm foo/bar/baz"
	        }
	      ]
	    }
	  ]
	}

## Running commands

There are configuration options for each command in the installation system:

### Custom directory

The current working directory can be set for each command, e.g.:

    {
       "command" : "touch file",
       "directory" : "$HOME"
    }

`$CWD` and `$HOME` are two specific codes that are substituted for the current working directory and the user's home directory respectively.

This only applies to a single command. The current working directory of subsequent commands is unchanged.

### Expected exit value

Normally, a command is expected to return `0` as an exit value. This can be modified on a per-command basis with `expectedExitValue`.

    {
       "command" : "touch file",
       "expectedExitValue" : 2
    }

### Extra path

The command path can be extended with `pathExtra`. The supplied path is in *addition* to the existing paths. 

    {
       "command" : "touch file",
       "pathExtra" : "/usr/local/bin"
    }
