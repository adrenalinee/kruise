cd package
helm package ../charts/kruise-standard-server
cd ../
helm repo index .