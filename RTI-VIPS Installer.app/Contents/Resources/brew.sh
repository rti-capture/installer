#!/bin/bash

SCRIPTDIR=`dirname "$BASH_SOURCE"`
BREW=/usr/local/bin/brew

export PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:$PATH

export SUDO_ASKPASS="$SCRIPTDIR/askpass.sh"

$BREW $*
