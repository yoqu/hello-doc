#!/usr/bin/env bash
source /etc/profile
PARAM=$1
cd ../
if [ "$PARAM" ]; then
echo "跳过npm run build方法，直接copy"
else
npm run build
fi


rm -rf java-plugins/maven-plugin/src/main/resources/static/*
rm -rf java-plugins/hello-docs-runner/src/main/resources/hello-docs/*
echo "clear old html cache success"
rm -rf dist/static/data/*
cp -R dist/* java-plugins/maven-plugin/src/main/resources/static/
cp -R dist/* java-plugins/hello-docs-runner/src/main/resources/hello-docs
rm -rf java-plugins/hello-docs-runner/src/main/resources/hello-docs/docs
echo "generate html success."
