#!/bin/bash

# start of the openroberta server with the database EMBEDDED.
# typical use case: small standalone servers, e.g. Raspberry PI. When the server is running, the database cannot
# be accessed by a sql client.
# if the server runs version x.y.z, the the database is expected in directory db-x.y.z
# note: if the database version is lower than the server version, first the database is upgraded
# admin responsibilities:
# - avoid log files to grow and grow ...
# - remove old database directories after successful upgrade

# DO NOT use this script with DOCKER, see the docker directory for alternatives

function propagateSignal() { 
  echo "Caught signal. Propagate this to child process $child" 
  kill -TERM "$child" 2>/dev/null
}

SERVERLOGFILE='./ora-server.log'

echo 'start-ora-server-with-db-embedded.sh with the following optional parameter:'
echo "  -logserver <file-name>     server log file. Default: $SERVERLOGFILE"
echo '  all parameters after these parameters are passed to the server'

while [ true ]
do
	case "$1" in
	  --logserver) SERVERLOGFILE=$2
                   shift; shift ;;
	  *)	       break ;;
	esac
done

echo 'check for database upgrade'
java -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade . >>$SERVERLOGFILE 2>&1

echo 'start the server with embedded database'
trap propagateSignal SIGTERM SIGINT
java  -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
	  -d database.parentdir=. -d database.mode=embedded $* >>$SERVERLOGFILE 2>&1 &
child=$!
wait "$child"
