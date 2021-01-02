rootProject.name = "simple-rpc"
include("simple-rpc-common")
include("simple-rpc-client")
include("simple-rpc-server")
include("sample:client-sample")
findProject(":sample:client-sample")?.name = "client-sample"
include("sample:server-sample")
findProject(":sample:server-sample")?.name = "server-sample"