#!/bin/bash

SCRIPTDIR=`dirname "$BASH_SOURCE"`
BREW=/usr/local/bin/brew

export SUDO_ASKPASS="$SCRIPTDIR/askpass.sh"

$BREW $*
