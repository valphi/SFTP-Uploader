#!/bin/bash
jpackage --input build/libs/ \
  --name SftpClient \
  --main-jar SftpClient.jar \
  --main-class com.sismo.demo.SftpClient \
  --type dmg \
  --java-options "-DSFTP_SERVER=s-75f7a0622f7c4478a.server.transfer.eu-west-1.amazonaws.com \
                    -DSFTP_USER=user \
                    -DSFTP_PHRASE=password \
                    -DSFTP_PRIVATE_KEY=~/.ssh/id_rsa \
                    -DSFTP_SERVER_DIRECTORY=user_indicator \
                    -DSFTP_LOCAL_DIRECTORY=~/sftp-sismo"