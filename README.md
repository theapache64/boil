# boil 🔥

> Boil makes copy-paste even easier 🤗

Boil is a CLI companion to add boilerplate codes to your gradle project

## Demo

![](demo.gif)

- [Watch Live Demo](https://www.youtube.com/watch?v=PTdgPnIU1tE&feature=youtu.be)


## Install ⚙️


````shell
sudo npm install -g boil-tool
````

## Usage ⌨️

- Step 1 : Find group name from [here](https://docs.google.com/spreadsheets/d/1OF384yi-k3iBgiyLnhYDAoYAV8wJGCh2yEqm3MfQQko/edit?usp=sharing)

- Step 2 : Execute `boil add <groupName>` from project root

_eg:_

```shell script
~$ boil add sle # To add SingeLiveEvent
```

## How to contribute more boilerplate codes?

- Add files to `files` directory
- Open a PR 

That's it!

## Environment Variables 🌍

- `$PACKAGE_NAME`  To get project package name

## Update ⤴️

```shell script
wget "https://raw.githubusercontent.com/theapache64/boil/master/update.sh" -q --show-progress -O update.sh && sh update.sh
```

## Author ✍️

- theapache64
