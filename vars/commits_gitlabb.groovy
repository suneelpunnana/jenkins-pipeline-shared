import groovy.json.*
import groovy.json.JsonSlurper 
//int ids1;

def call(jsondata){
      def jsonString = jsondata
      def jsonObj = readJSON text: jsonString
      String a=jsonObj.scm1.projects.project.project_id
String Name=a.replaceAll("\\[", "").replaceAll("\\]","");
     withCredentials([usernamePassword(credentialsId: 'gitlab_cred', passwordVariable: 'password', usernameVariable:'username')]) {
      sh "curl -X GET    -u $username:$password https://gitlab.com/api/v4/users/5418155/projects -o outputgitlab.json"
     }
   def jsonSlurper = new JsonSlurper()
 def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/outputgitlab.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
def usertotal = resultJson.size()
      println(usertotal)
      println(Name)
      for(i=0;i<usertotal;i++)
         {
            if(Name==resultJson[i].name)
            {
               def id1 = resultJson[i].id 
               println(id1)
             return id1
            }
         }
         }
def commit(ids1,jsondata){
	def jsonString = jsondata
def jsonObj = readJSON text: jsonString
      println(ids1)
	int ecount = jsonObj.config.emails.email.size()
         println("No of users "+ ecount)
      withCredentials([usernamePassword(credentialsId: 'gitlab_cred', passwordVariable: 'password', usernameVariable:'username')]) {
	      sh "curl -X GET   -u $username:$password https://gitlab.com/api/v4/projects/${ids1}/repository/commits -o outputgitlab.json"
      }
   def jsonSlurper = new JsonSlurper()
   def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/outputgitlab.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
def total = resultJson.size()
   println(total)
	println(ecount)
      //println(JsonOutput.toJson(resultJson))
      List<String> JSON = new ArrayList<String>();
   	 List<String> LIST = new ArrayList<String>();
	 List<String> LIST1 = new ArrayList<String>();

	 def jsonBuilder = new groovy.json.JsonBuilder()
for(i=0;i<ecount;i++)
 {
	def email=jsonObj.config.emails.email[i] 
  for(j=0;j<total;j++)
  {
	 // println(jsonObj.config.emails.email[i])
	 // println(resultJson[j].author_email)
   if(email==resultJson[j].author_email)
   {
	   JSON.add(resultJson[j])
	 // JSON.add(resultJson[j])
     }
     }
	// println(jsonObj.config.emails.email[i])
	 cnt=JSON.size()
	 LIST1[i]=JSON.clone()
	 LIST.add(["email":email,"Commit": LIST1[i],"Commit_cnt":cnt])
	//LIST.add(["email":email,"Commit":JSON,"Commit_cnt":cnt])
	 //JCOPY[i]=(JsonOutput.toJson(JSON))
	// println(JCOPY[i])
	 JSON.clear()
	 
	   
     }
 /* for(i=0;i<JCOPY.size();i++)
	{
		println(JCOPY[i])
	}
    */
 jsonBuilder.gitlab(
  "total_commit" : resultJson,
  "commit_cnt" : resultJson.size(),
	 "individual_commit_Details":LIST
  
  )
File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/gitlabcommit.json")
	file.write(jsonBuilder.toPrettyString())

return jsonBuilder
}
