using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace meditatio_client
{
    public partial class FormLogin : Form
    {
        public FormLogin()
        {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private async void btnLogin_Click(object sender, EventArgs e)
        {
            this.Text = "Autentificare în curs...";
            btnLogin.Enabled = false;

            try
            {
                JwtAuthResponse authData = await ApiService.LoginAsync(txtUsername.Text, txtPassword.Text);

                if (authData != null && authData.Roles.Contains("ROLE_ADMIN"))
                {
                    // ESTE ADMIN! Deschidem panoul de control
                    FormAdmin adminForm = new FormAdmin();
                    adminForm.Show();
                    this.Hide(); // Ascundem formularul de login
                }
                else if (authData != null)
                {
                    MessageBox.Show("Autentificare reușită, dar nu aveți drepturi de administrator.",
                                    "Acces Interzis", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
                else
                {
                    MessageBox.Show("Username sau parolă incorectă.",
                                    "Login Eșuat", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare de conexiune: {ex.Message}",
                                "Eroare Server", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }

            this.Text = "Login Admin";
            btnLogin.Enabled = true;
        }

        private void FormLogin_Load(object sender, EventArgs e)
        {

        }
    }
    
}
