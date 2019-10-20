#!/bin/bash 

function to_number()
{
	r="$1"
	r=$(sed -r 's/^0+([1-9])/\1/' <<<"$r")
	echo $r
}

dryrun=${5:-"no"}

SAISON_A_TRAITER=$( to_number $1)
PREMIER_EPISODE_SAISON=$( to_number $2)
DERNIER_EPISODE_SAISON=$( to_number $3)
NOUVEL_EPISODE_START=$( to_number $4)


IFS=$'\n'
for I in $(find . -type f -regex ".*.S[0-9][0-9]E[0-9]+..*" | sort -n | grep -v ".@__thumb")
do
	
	SAISON=$( to_number $(echo $I | sed -e "s/.*.S\([0-9][0-9]\)E\([0-9]\+\)..*/\1/g") )
	EPISODE=$( to_number $(echo $I | sed -e "s/.*.S\([0-9][0-9]\)E\([0-9]\+\)..*/\2/g") )
	if [ $PREMIER_EPISODE_SAISON -le $EPISODE ] && [ $DERNIER_EPISODE_SAISON -ge $EPISODE ]
	then
		NOUVEL_EPISODE=$[ $EPISODE - $PREMIER_EPISODE_SAISON + $NOUVEL_EPISODE_START ]
		DEBUT=$(echo $I | sed -e "s/\(.*\).S[0-9][0-9]E[0-9]\+.\(.*\)/\1/g")
		FIN=$(echo $I | sed -e "s/\(.*\).S[0-9][0-9]E[0-9]\+.\(.*\)/\2/g")
		
		#echo "mv $I ${DEBUT}.S${SAISON_A_TRAITER}E${NOUVEL_EPISODE}.${FIN}"
	
		new_file=$(printf "${DEBUT}.S%02dE%02d.${FIN}\n" $SAISON_A_TRAITER $NOUVEL_EPISODE)
		if [ -e "$new_file" ]
		then
			echo "$I => $new_file already exist"
			exit 1
		fi
	
		cmd=$(printf "mv \"$I\" \"${DEBUT}.S%02dE%02d.${FIN}\"\n" $SAISON_A_TRAITER $NOUVEL_EPISODE)
		echo $cmd
		
		if [ "$dryrun" == "no" ]
                then
                       eval $cmd
                fi
	fi
done
