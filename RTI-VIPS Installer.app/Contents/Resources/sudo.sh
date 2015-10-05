#!/bin/bash

SCRIPTDIR=`dirname "$BASH_SOURCE"`

export SUDO_ASKPASS="$SCRIPTDIR/askpass.sh"

sudo -A $*
