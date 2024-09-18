package MK.CVForYou;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeekAppliedJob
{
    public String job_id;
    public String job_title;
    public ArrayList<String> status;
    public boolean active;
    public String company_name;
    public String company_id;

    public SeekAppliedJob(JSONObject node)
    {
        job_id = node.optString("id");
        active = node.optBoolean("isActive");

        JSONObject job = (JSONObject) node.optQuery("/job");
        if(job != null)
        {
            job_title = job.optString("title");
            JSONObject advertiser = (JSONObject) job.optQuery("/advertiser");
            if(advertiser != null)
            {
                company_name = advertiser.optString("name");
                company_id = advertiser.optString("id");
            }
        }


        status = new ArrayList<String>(3);
        JSONArray events = (JSONArray) node.query("/events");
        if(events != null)
        {
            Iterator<Object> events_itr = events.iterator();
            while(events_itr.hasNext())
            {
                JSONObject event = (JSONObject)events_itr.next();
                String status_value = event.optString("status");
                if(status_value != null)
                    status.add( status_value  );
            }
        }

    }


    public String getLastestStatus()
    {
        if(status.size() > 0)
            return status.get(status.size()-1);
        return null;
    }

    public String toCSV()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("'%s',", job_id));
        sb.append(String.format("'%s',", job_title));
        sb.append(String.format("'%s',", active));
        sb.append(String.format("'%s',", company_name));
        sb.append(String.format("'%s',", company_id));
        sb.append(String.format("'%s',", getLastestStatus()));
        sb.append("\n");

        return sb.toString();
    }

}



            //JSONArray saved_jobs = (JSONArray) object.query("/data/viewer/appliedJobs/edges");
            //if(saved_jobs == null)
            //    throw new NullPointerException();

            //Iterator<Object> job_itr = saved_jobs.iterator();
            //while(job_itr.hasNext())
            //{
            //    JSONObject job = (JSONObject)job_itr.next();
            //    arr.add(new SeekAppliedJob(job.getJSONObject("node")));
            //}









//{
//    "data": {
//        "viewer": {
//            "id": 40969211,
//            "appliedJobs": {
//                "edges": [
//                    {
//                        "node": {
//                            "id": "78816330",
//                            "hasAppliedWithResume": true,
//                            "hasAppliedWithCoverLetter": true,
//                            "isExternal": false,
//                            "isActive": true,
//                            "notes": "",
//                            "events": [
//                                {
//                                    "status": "Applied",
//                                    "timestamp": {
//                                        "dateTimeUtc": "2024-09-16T07:59:15.858Z",
//                                        "shortAbsoluteLabel": "16 Sep 2024",
//                                        "__typename": "SeekDateTime"
//                                    },
//                                    "__typename": "AppliedJobEvent"
//                                }
//                            ],
//                            "appliedAt": {
//                                "dateTimeUtc": "2024-09-16T07:59:15.858Z",
//                                "shortAbsoluteLabel": "16 Sep 2024",
//                                "__typename": "SeekDateTime"
//                            },
//                            "job": {
//                                "id": "78816330",
//                                "title": "Software Engineer",
//                                "location": {
//                                    "label": "Sydney NSW",
//                                    "__typename": "LocationInfo"
//                                },
//                                "abstract": "Mid level Software Engineer to design and develop solutions using .NET, React/Angular, and AWS. Collaborate, mentor, and grow in a dynamic team.",
//                                "createdAt": {
//                                    "dateTimeUtc": "2024-09-13T03:34:38.000Z",
//                                    "label": "4d ago",
//                                    "__typename": "SeekDateTime"
//                                },
//                                "advertiser": {
//                                    "id": "31436089",
//                                    "name": "Motion Recruitment",
//                                    "__typename": "Advertiser"
//                                },
//                                "salary": null,
//                                "products": {
//                                    "branding": {
//                                        "logo": {
//                                            "url": "https://image-service-cdn.seek.com.au/73b6f42a39199bd2ecc9c3b5d318f7903e307e81/f3c5292cec0e05e4272d9bf9146f390d366481d0",
//                                            "__typename": "JobProductBrandingImage"
//                                        },
//                                        "__typename": "JobProductBranding"
//                                    },
//                                    "__typename": "JobProducts"
//                                },
//                                "tracking": {
//                                    "hasRoleRequirements": true,
//                                    "__typename": "JobTracking"
//                                },
//                                "__typename": "Job"
//                            },
//                            "__typename": "AppliedJob"
//                        },
//                        "__typename": "AppliedJobEdge"
//                    }
//                ]
//            }
//        }
//    }
//}


//public class SeekSavedJob
//{
//    public String job_id = "";
//    public Boolean isActive = null; //it would be nice if this was never null
//    public String notes = "";
//    public Boolean isExternal = null; //it would be nice if this was never null
//    public String title = "";
//    public String location = "";
//    public String job_abstract = "";
//    public String company = "";
//    public String salary = "";
//
//    public SeekSavedJob(JSONObject node)
//    {
//        job_id = node.optString("id");
//        isActive =  node.optBooleanObject("isActive", null);
//        notes = node.optString("notes");
//        isExternal =  node.optBooleanObject("isExternal", null);
//
//
//        JSONObject job = (JSONObject) node.optQuery("/job");
//        if(job == null)
//            return;
//        title = job.optString("title");
//        job_abstract = job.optString("abstract");
//        
//
//
//        JSONObject location_object = (JSONObject) job.optQuery("/location");
//        if(location_object != null)
//            location = location_object.optString("label");
//
//        JSONObject advertiser = (JSONObject) job.optJSONObject("advertiser");
//        if(advertiser == null)
//            return;
//        company = advertiser.optString("name");
//
//
//        JSONObject salaryJson = job.optJSONObject("salary");
//        if (salaryJson != null) {
//            salary = salaryJson.optString("label");
//        }
//    }
//
//    public String getID()
//    {
//        return job_id;
//    }
//}

