using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Net.Http;
using System.Net.Http.Headers;


namespace meditatio_client
{
    public static class ApiService
    {
        // ================================================================
        // PARTEA CARE LIPSEA: Definirea și Inițializarea 'client'
        // ================================================================

        // Aici definim 'client'-ul pe care îl vei folosi peste tot
        private static readonly HttpClient client = new HttpClient();
        private static string jwtToken = "";

        // Constructorul static rulează o singură dată și setează adresa serverului
        static ApiService()
        {
            client.BaseAddress = new System.Uri("http://localhost:9090");
        }

        // ================================================================
        // METODA DE LOGIN (pe care ai vrut să o păstrezi)
        // ================================================================

        public static async Task<JwtAuthResponse> LoginAsync(string username, string password)
        {
            var loginRequest = new { username, password };
            var json = JsonConvert.SerializeObject(loginRequest);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await client.PostAsync("/api/auth/login", content);

            if (!response.IsSuccessStatusCode)
            {
                return null; // Login eșuat
            }

            var jsonResponse = await response.Content.ReadAsStringAsync();
            var authData = JsonConvert.DeserializeObject<JwtAuthResponse>(jsonResponse);

            // Stocăm token-ul pentru cererile viitoare
            jwtToken = authData.Token;
            client.DefaultRequestHeaders.Authorization =
                new AuthenticationHeaderValue("Bearer", jwtToken);

            return authData;
        }

        // ================================================================
        // METODELE PENTRU ADMIN (scrise de tine)
        // ================================================================
        // Acum vor funcționa, deoarece 'client' este definit mai sus

        public static async Task<List<User>> GetStudentsAsync()
        {
            var response = await client.GetAsync("/api/admin/users/students");
            response.EnsureSuccessStatusCode(); // Aruncă eroare dacă nu e 200 OK
            var json = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<List<User>>(json);
        }

        public static async Task<List<User>> GetProfessorsAsync()
        {
            var response = await client.GetAsync("/api/admin/users/professors");
            response.EnsureSuccessStatusCode();
            var json = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<List<User>>(json);
        }

        public static async Task<List<Assignment>> GetAllAssignmentsAsync()
        {
            var response = await client.GetAsync("/api/admin/assignments");
            response.EnsureSuccessStatusCode();
            var json = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<List<Assignment>>(json);
        }

        public static async Task<bool> DeleteUserAsync(int userId)
        {
            var response = await client.DeleteAsync($"/api/admin/users/{userId}");
            return response.IsSuccessStatusCode;
        }

        public static async Task<bool> DeleteAssignmentAsync(int assignmentId)
        {
            var response = await client.DeleteAsync($"/api/admin/assignments/{assignmentId}");
            return response.IsSuccessStatusCode;
        }

        public static async Task<User> CreateUserAsync(string user, string pass, string role)
        {
            var request = new CreateUserRequest { username = user, password = pass, roleName = role };
            var json = JsonConvert.SerializeObject(request);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await client.PostAsync("/api/admin/users/create", content);
            response.EnsureSuccessStatusCode();

            var jsonResponse = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<User>(jsonResponse);
        }
    }
}
