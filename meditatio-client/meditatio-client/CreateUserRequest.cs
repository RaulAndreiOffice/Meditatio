using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace meditatio_client
{
    public class CreateUserRequest
    {
        public string username { get; set; }
        public string password { get; set; }
        public string roleName { get; set; } // ex: "ROLE_STUDENT"

    }
}
