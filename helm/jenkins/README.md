# jenkins

- chart: https://artifacthub.io/packages/helm/jenkinsci/jenkins
- version: 4.3.30


```shell
helm upgrade --install --create-namespace -f values.yaml -n jenkins jenkins jenkins/jenkins --version 4.3.30 
```