<div style="text-align:center"><a href="https://github.com/1337Zero/ProPlan"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/FullLogo.png?raw=true" title="ProPlan" alt="ProPlan"></a></div>

# ProPlan
> Your small project planing tool.

Tired of big complex excel sheets ? Try ProPlan.

Features are:

- An easy graphical User Interface to work with your project plan.
- Based on java programming language, it can be executed on any system which has the java runtime installed.
- Automated calculation of following values: early start date, late start date, early finish date, late finish date, total buffer and free buffer
- Displays the critical path as red drawn Processes and connections
- Changeable language file to support different languages
- Bugs
- Open Source


## Installation

- Make sure you have at least java 8 installed on your computer.
- Download the latest release from the <a href="https://github.com/jsnh19/ProPlan/releases">Realease Page</a>

### Clone
> Want to build it yourself ?
- Clone this Repository to your local machine using 

```shell
$ git clone https://github.com/jsnh19/ProPlan.git
```
- Make sure that you linked the needed libraries before starting the build process.

### Setup

- Just start the downloaded ProPlan.jar with a double click on the icon.
- If it won't start, probably because you messed up your java home path, you can start the jar with the following commands:

```shell
$ cd downloads
$ java -jar ProPlan.jar
```

> ProPlan will create 2 Files inside a folder in your home directory.
> The folder is called ProPlan  and the files inside are: 
> - language.json, contains all the language settings needed, default language is German
> - settings.json, contains some settings which you can edit



## Features

- Easy Overview over a project plan.

> - Red Marked Processes and arrows are displaying the critical path.
> - The * behind the Plan's name tells you, that you have unchanged changes.
> - The footer holds a coordinate System telling you how far up and down, left and right you moved the plan.

<div style="text-align:center"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/overview.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>


#### Creation of a new Plan

> - Fast and easy
> - Only the Name of the new Plan is needed
> - Add an optional description to store some information about your new plan 

<div style="text-align:center"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/newplan.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>


#### Creation of a new Process

> Easy creation of a new Process
> Needed values are:
> - The ID of the new Process, ProPlan will give you by default the next free ID, but you can change it following your needs. 
> - The ID has to be *unique* for the edited Plan

> - Everything can be used as a Name for a Process, please note that too long names will be shorten inside the GUI

> - Duration is the length of your new Process

> - If your Process is the last Process of your Plan, please mark is as end. Its important to have an end and a begin.
> - ProPlan will assume that the first Process  will be the start of your plan.

<div style="text-align:center"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/newprocess.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>

> - Inside the tab predecessor, you can add a predecessor for the new Process. The new Process will be automatically a successor of each predecessor.

> - As you can see you can choose between all added processes. To see a difference between same name processes the ID will be added to the name of the processes.
> - Press the add Button to add a selected process, use the remove button to remove the selected process inside the process list.

> - Press Ok to add the new process

<div style="text-align:center"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/newprocess2.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>

#### Loading of a stored Plan

You can load a Plan out of 2 predefined file types

> - .plan

> .plan files are files stored with java serialization. That files are basically cut out content of your memory.

> *Please note that this file type might not be future proof. If the stored java classes are changed, after an update, it might fail to load them.*

---

> - .plson

> .plson files are plain .json files which are compressed via GZIP. You can edit them if you want by opening them with your preferred ZIP-Manager, which supports GZIP, and edit the file inside with a preferred text-editor.

> This file-type should be much more future proof than the .plan type.

<div style="text-align:center"><https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/open.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>

#### Storing a plan

As you read above ProPlan supports 2 file types

> .plson and .plan

> ProPlan will store the last stored/loaded file and will open that path if you load or save a plan.

<div style="text-align:center"><https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/save.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>

### Edit a Process

If you want to edit an all ready created Process you got different ways to do so. It is working similar to creating a new process.

> - Click the edit, *pen* icon, to start the edit mode.
> - If you hover over a process your mouse icon will change to the *pen* icon.
> - Now left click that process to open the edit screen

> Another even easier way to open the edit sreen is the following:
> - Double click the Process you want to edit.

> If everything of the build in functions isn't helping you, you are able to edit the stored .plson file. Open it with a GZIP compatible File Manager and edit the file inside as you need.

<div style="text-align:center"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/editprocess.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>

### Delete a Process

Similar to the edit process you can delete a process

> - Click the delete, *trash* icon, to start the delete mode.
> - If you hover over a process your mouse icon will change to the *trash* icon.
> - Now left click that process to delete it

### Open the help page

The help page is displaying information about the running ProPlan version and about the loaded plan

> Inside the tab *Information about the current plan* you can see the name of the loaded plan and the description stored for it

> Inside the tab *Information about ProPlan* you can see the author and the version of ProPlan.
> It also displays a small help text.

<div style="text-align:center"><img src="https://github.com/jsnh19/ProPlan/blob/master/src/ressources/images/plandescription.PNG?raw=true" title="ProPlan" alt="ProPlan"></div>

## Contributing

> You got multiple ways to help the developing ProPlan

##### Step 1

- **Option 1**
    - Fork this Repository and create your own ProPlan!

- **Option 2**
    - Clone this repo to your local machine using    
    
    $ git clone https://github.com/jsnh19/ProPlan.git    
    
- **Option 3**
	 - Create a <a href="https://github.com/jsnh19/ProPlan/issues">Bug Report / Feature Request</a> if you have any
	 
##### Step 2

- Fix **Bugs** on your own or add new **features**

##### Step 3

-  Create a pull request using <a href="https://github.com/jsnh19/ProPlan/compare" target="_blank">`https://github.com/jsnh19/ProPlan/compare`</a>.


## Donations

- If you wish to donate me some *pizza*, you are free to write me a message

## License

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2019 © <a href="https://github.com/1337Zero/" target="_blank">1337Zero</a>.>.