using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace meditatio_client
{
    public class Assignment
    {
        [JsonProperty("assid")]
        public int AssignmentId { get; set; }

        [JsonProperty("asstitle")]
        public string Title { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        // Putem adăuga și 'student' și 'professor' dacă mapăm clasele
    }
}
