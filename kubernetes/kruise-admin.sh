kubectl create serviceaccount kruise-admin -n kruise
kubectl create clusterrole kruise-admin-role --verb='*' --resource='*' -n kruise
kubectl create clusterrolebinding kruise-admin-role-binding --clusterrole kruise-admin-role --serviceaccount kruise:kruise-admin -n kruise
