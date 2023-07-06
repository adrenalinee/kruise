# argo-cd

- chart: https://artifacthub.io/packages/helm/argo/argo-cd
- version: 5.37.0


```shell
helm upgrade --install --create-namespace -f values.yaml -n argo-cd argo-cd argo/argo-cd --version 5.37.0 
```