namespace meditatio_client
{
    partial class FormAdmin
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.tabControlMain = new System.Windows.Forms.TabControl();
            this.tabPageStudents = new System.Windows.Forms.TabPage();
            this.tabPageProfessors = new System.Windows.Forms.TabPage();
            this.tabPageAssignments = new System.Windows.Forms.TabPage();
            this.dgvStudents = new System.Windows.Forms.DataGridView();
            this.btnDeleteStudent = new System.Windows.Forms.Button();
            this.btnCreateStudent = new System.Windows.Forms.Button();
            this.btnRefreshStudents = new System.Windows.Forms.Button();
            this.dgvProfessors = new System.Windows.Forms.DataGridView();
            this.btnDeleteProfessors = new System.Windows.Forms.Button();
            this.btnCreateProfessors = new System.Windows.Forms.Button();
            this.btnRefreshProfessors = new System.Windows.Forms.Button();
            this.dgvAssignments = new System.Windows.Forms.DataGridView();
            this.btnDeleteAssignments = new System.Windows.Forms.Button();
            this.btnRefreshAssignments = new System.Windows.Forms.Button();
            this.tabControlMain.SuspendLayout();
            this.tabPageStudents.SuspendLayout();
            this.tabPageProfessors.SuspendLayout();
            this.tabPageAssignments.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dgvStudents)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvProfessors)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvAssignments)).BeginInit();
            this.SuspendLayout();
            // 
            // tabControlMain
            // 
            this.tabControlMain.Controls.Add(this.tabPageStudents);
            this.tabControlMain.Controls.Add(this.tabPageProfessors);
            this.tabControlMain.Controls.Add(this.tabPageAssignments);
            this.tabControlMain.Location = new System.Drawing.Point(0, 0);
            this.tabControlMain.Name = "tabControlMain";
            this.tabControlMain.SelectedIndex = 0;
            this.tabControlMain.Size = new System.Drawing.Size(493, 334);
            this.tabControlMain.TabIndex = 2;
            // 
            // tabPageStudents
            // 
            this.tabPageStudents.Controls.Add(this.btnRefreshStudents);
            this.tabPageStudents.Controls.Add(this.btnCreateStudent);
            this.tabPageStudents.Controls.Add(this.btnDeleteStudent);
            this.tabPageStudents.Controls.Add(this.dgvStudents);
            this.tabPageStudents.Location = new System.Drawing.Point(4, 22);
            this.tabPageStudents.Name = "tabPageStudents";
            this.tabPageStudents.Padding = new System.Windows.Forms.Padding(3);
            this.tabPageStudents.Size = new System.Drawing.Size(485, 308);
            this.tabPageStudents.TabIndex = 0;
            this.tabPageStudents.Text = "Studenți";
            this.tabPageStudents.UseVisualStyleBackColor = true;
            // 
            // tabPageProfessors
            // 
            this.tabPageProfessors.Controls.Add(this.btnRefreshProfessors);
            this.tabPageProfessors.Controls.Add(this.btnCreateProfessors);
            this.tabPageProfessors.Controls.Add(this.btnDeleteProfessors);
            this.tabPageProfessors.Controls.Add(this.dgvProfessors);
            this.tabPageProfessors.Location = new System.Drawing.Point(4, 22);
            this.tabPageProfessors.Name = "tabPageProfessors";
            this.tabPageProfessors.Padding = new System.Windows.Forms.Padding(3);
            this.tabPageProfessors.Size = new System.Drawing.Size(485, 308);
            this.tabPageProfessors.TabIndex = 1;
            this.tabPageProfessors.Text = "Profesori";
            this.tabPageProfessors.UseVisualStyleBackColor = true;
            // 
            // tabPageAssignments
            // 
            this.tabPageAssignments.Controls.Add(this.btnRefreshAssignments);
            this.tabPageAssignments.Controls.Add(this.btnDeleteAssignments);
            this.tabPageAssignments.Controls.Add(this.dgvAssignments);
            this.tabPageAssignments.Location = new System.Drawing.Point(4, 22);
            this.tabPageAssignments.Name = "tabPageAssignments";
            this.tabPageAssignments.Size = new System.Drawing.Size(485, 308);
            this.tabPageAssignments.TabIndex = 2;
            this.tabPageAssignments.Text = "Teme";
            this.tabPageAssignments.UseVisualStyleBackColor = true;
            // 
            // dgvStudents
            // 
            this.dgvStudents.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvStudents.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dgvStudents.Location = new System.Drawing.Point(3, 3);
            this.dgvStudents.Name = "dgvStudents";
            this.dgvStudents.Size = new System.Drawing.Size(479, 302);
            this.dgvStudents.TabIndex = 0;
            // 
            // btnDeleteStudent
            // 
            this.btnDeleteStudent.Location = new System.Drawing.Point(107, 279);
            this.btnDeleteStudent.Name = "btnDeleteStudent";
            this.btnDeleteStudent.Size = new System.Drawing.Size(75, 23);
            this.btnDeleteStudent.TabIndex = 1;
            this.btnDeleteStudent.Text = "Șterge Student";
            this.btnDeleteStudent.UseVisualStyleBackColor = true;
            this.btnDeleteStudent.Click += new System.EventHandler(this.btnDeleteStudent_Click_1);
            // 
            // btnCreateStudent
            // 
            this.btnCreateStudent.Location = new System.Drawing.Point(188, 278);
            this.btnCreateStudent.Name = "btnCreateStudent";
            this.btnCreateStudent.Size = new System.Drawing.Size(75, 23);
            this.btnCreateStudent.TabIndex = 2;
            this.btnCreateStudent.Text = "Adaugă Student";
            this.btnCreateStudent.UseVisualStyleBackColor = true;
            this.btnCreateStudent.Click += new System.EventHandler(this.btnCreateStudent_Click_1);
            // 
            // btnRefreshStudents
            // 
            this.btnRefreshStudents.Location = new System.Drawing.Point(269, 279);
            this.btnRefreshStudents.Name = "btnRefreshStudents";
            this.btnRefreshStudents.Size = new System.Drawing.Size(75, 23);
            this.btnRefreshStudents.TabIndex = 3;
            this.btnRefreshStudents.Text = "Refresh";
            this.btnRefreshStudents.UseVisualStyleBackColor = true;
            // 
            // dgvProfessors
            // 
            this.dgvProfessors.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvProfessors.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dgvProfessors.Location = new System.Drawing.Point(3, 3);
            this.dgvProfessors.Name = "dgvProfessors";
            this.dgvProfessors.Size = new System.Drawing.Size(479, 302);
            this.dgvProfessors.TabIndex = 0;
            // 
            // btnDeleteProfessors
            // 
            this.btnDeleteProfessors.Location = new System.Drawing.Point(106, 278);
            this.btnDeleteProfessors.Name = "btnDeleteProfessors";
            this.btnDeleteProfessors.Size = new System.Drawing.Size(75, 23);
            this.btnDeleteProfessors.TabIndex = 1;
            this.btnDeleteProfessors.Text = "Șterge Profesor";
            this.btnDeleteProfessors.UseVisualStyleBackColor = true;
            this.btnDeleteProfessors.Click += new System.EventHandler(this.btnDeleteProfessors_Click);
            // 
            // btnCreateProfessors
            // 
            this.btnCreateProfessors.Location = new System.Drawing.Point(187, 278);
            this.btnCreateProfessors.Name = "btnCreateProfessors";
            this.btnCreateProfessors.Size = new System.Drawing.Size(75, 23);
            this.btnCreateProfessors.TabIndex = 2;
            this.btnCreateProfessors.Text = "Adaugă ";
            this.btnCreateProfessors.UseVisualStyleBackColor = true;
            this.btnCreateProfessors.Click += new System.EventHandler(this.btnCreateProfessors_Click);
            // 
            // btnRefreshProfessors
            // 
            this.btnRefreshProfessors.Location = new System.Drawing.Point(268, 278);
            this.btnRefreshProfessors.Name = "btnRefreshProfessors";
            this.btnRefreshProfessors.Size = new System.Drawing.Size(75, 23);
            this.btnRefreshProfessors.TabIndex = 3;
            this.btnRefreshProfessors.Text = "Refresh";
            this.btnRefreshProfessors.UseVisualStyleBackColor = true;
            // 
            // dgvAssignments
            // 
            this.dgvAssignments.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvAssignments.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dgvAssignments.Location = new System.Drawing.Point(0, 0);
            this.dgvAssignments.Name = "dgvAssignments";
            this.dgvAssignments.Size = new System.Drawing.Size(485, 308);
            this.dgvAssignments.TabIndex = 0;
            // 
            // btnDeleteAssignments
            // 
            this.btnDeleteAssignments.Location = new System.Drawing.Point(152, 278);
            this.btnDeleteAssignments.Name = "btnDeleteAssignments";
            this.btnDeleteAssignments.Size = new System.Drawing.Size(75, 23);
            this.btnDeleteAssignments.TabIndex = 1;
            this.btnDeleteAssignments.Text = "Șterge";
            this.btnDeleteAssignments.UseVisualStyleBackColor = true;
            this.btnDeleteAssignments.Click += new System.EventHandler(this.btnDeleteAssignments_Click);
            // 
            // btnRefreshAssignments
            // 
            this.btnRefreshAssignments.Location = new System.Drawing.Point(233, 278);
            this.btnRefreshAssignments.Name = "btnRefreshAssignments";
            this.btnRefreshAssignments.Size = new System.Drawing.Size(75, 23);
            this.btnRefreshAssignments.TabIndex = 3;
            this.btnRefreshAssignments.Text = "Refresh";
            this.btnRefreshAssignments.UseVisualStyleBackColor = true;
            // 
            // FormAdmin
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(493, 335);
            this.Controls.Add(this.tabControlMain);
            this.Name = "FormAdmin";
            this.Text = "FormAdmin";
            this.Load += new System.EventHandler(this.FormAdmin_Load);
            this.tabControlMain.ResumeLayout(false);
            this.tabPageStudents.ResumeLayout(false);
            this.tabPageProfessors.ResumeLayout(false);
            this.tabPageAssignments.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.dgvStudents)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvProfessors)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvAssignments)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion
        private System.Windows.Forms.TabControl tabControlMain;
        private System.Windows.Forms.TabPage tabPageStudents;
        private System.Windows.Forms.TabPage tabPageProfessors;
        private System.Windows.Forms.TabPage tabPageAssignments;
        private System.Windows.Forms.Button btnRefreshStudents;
        private System.Windows.Forms.Button btnCreateStudent;
        private System.Windows.Forms.Button btnDeleteStudent;
        private System.Windows.Forms.DataGridView dgvStudents;
        private System.Windows.Forms.Button btnRefreshProfessors;
        private System.Windows.Forms.Button btnCreateProfessors;
        private System.Windows.Forms.Button btnDeleteProfessors;
        private System.Windows.Forms.DataGridView dgvProfessors;
        private System.Windows.Forms.Button btnRefreshAssignments;
        private System.Windows.Forms.Button btnDeleteAssignments;
        private System.Windows.Forms.DataGridView dgvAssignments;
    }
}