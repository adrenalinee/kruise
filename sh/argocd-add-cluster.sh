argocd cluster add $1 --name $2 --kubeconfig $3 --server $4
#argocd cluster add ${contextName} \
#--name ${contextName} \
#--kubeconfig ${kubeconfig} \
#--plaintext \
#--server ${argocdServer} \
#--upsert \
#--auth-token ${argocdToken}