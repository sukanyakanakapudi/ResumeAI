
import React, { useState } from "react";
import axios from "axios";
import "./App.css";

const API = "http://localhost:8080/api";

function App() {
  const [candidateName, setCandidateName] = useState("");
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState("");
  const [resumeId, setResumeId] = useState(null);
  const [result, setResult] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  // Upload resume PDF
  const uploadResume = async () => {
    if (!candidateName.trim() || !file) {
      setMessage("Please enter your name and select a PDF resume.");
      return;
    }

    if (file.type !== "application/pdf") {
      setMessage("Please select a PDF file.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("candidateName", candidateName);

    try {
      setLoading(true);
      setMessage("");

      const response = await axios.post(
        `${API}/resumes/upload`,
        formData
      );

      setResumeId(response.data.id);
      setResult(null);
      setMessage("Resume uploaded successfully!");
    } catch (error) {
      setMessage(
        error.response?.data?.message ||
        "Upload failed. Check whether backend is running."
      );
    } finally {
      setLoading(false);
    }
  };

  // Analyze uploaded resume
  const analyzeResume = async () => {
    if (!resumeId) {
      setMessage("Please upload your resume first.");
      return;
    }

    if (!jobDescription.trim()) {
      setMessage("Please enter the job description.");
      return;
    }

    try {
      setLoading(true);
      setMessage("");

      const response = await axios.post(
        `${API}/analysis/${resumeId}`,
        { jobDescription }
      );

      setResult(response.data);
      setMessage("Resume analysis completed!");
    } catch (error) {
      setMessage(
        error.response?.data?.message ||
        "Analysis failed. Please check the backend."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <div className="brand">ResumeAI</div>
        <span>Smart Resume Analyzer</span>
      </header>

      <main className="container">
        <section className="hero">
          <p className="eyebrow">AI-POWERED CAREER TOOL</p>
          <h1>Make Your Resume Job-Ready</h1>
          <p>
            Upload your resume, compare it with a job description,
            and discover skills you can improve.
          </p>
        </section>

        <section className="panel">
          <h2>1. Upload Your Resume</h2>

          <label>Candidate Name</label>
          <input
            type="text"
            placeholder="Enter your name"
            value={candidateName}
            onChange={(e) => setCandidateName(e.target.value)}
          />

          <label>Resume PDF</label>
          <input
            type="file"
            accept=".pdf,application/pdf"
            onChange={(e) => {
              setFile(e.target.files[0] || null);
              setResumeId(null);
              setResult(null);
            }}
          />

          <button onClick={uploadResume} disabled={loading}>
            {loading ? "Please wait..." : "Upload Resume"}
          </button>

          {resumeId && (
            <p className="success">
              Resume uploaded! Resume ID: {resumeId}
            </p>
          )}
        </section>

        <section className="panel">
          <h2>2. Enter Job Description</h2>

          <label>Job Description</label>
          <textarea
            rows="7"
            placeholder="Paste the job description here..."
            value={jobDescription}
            onChange={(e) => setJobDescription(e.target.value)}
          />

          <button
            onClick={analyzeResume}
            disabled={loading || !resumeId}
          >
            {loading ? "Analyzing..." : "Analyze Resume"}
          </button>
        </section>

        {message && <p className="message">{message}</p>}

        {result && (
          <section className="results">
            <h2>3. Your ATS Analysis</h2>
            <p className="candidate">
              Candidate: {result.candidateName}
            </p>

            <div className="score-card">
              <p>ATS Skill Match Score</p>
              <div className="score">{result.atsScore}%</div>

              <div className="progress">
                <div
                  className="progress-fill"
                  style={{
                    width: `${Math.max(
                      0,
                      Math.min(100, result.atsScore)
                    )}%`
                  }}
                />
              </div>

              <small>
                Based on the skills checked by this project's analyzer.
              </small>
            </div>

            <div className="skill-grid">
              <div className="skill-card">
                <h3>Matching Skills</h3>
                {result.matchingSkills.length > 0 ? (
                  result.matchingSkills.map((skill) => (
                    <span className="skill good" key={skill}>
                      ✓ {skill}
                    </span>
                  ))
                ) : (
                  <p>No matching skills found.</p>
                )}
              </div>

              <div className="skill-card">
                <h3>Missing Skills</h3>
                {result.missingSkills.length > 0 ? (
                  result.missingSkills.map((skill) => (
                    <span className="skill missing" key={skill}>
                      + {skill}
                    </span>
                  ))
                ) : (
                  <p>No missing skills found.</p>
                )}
              </div>
            </div>

            <div className="skill-card suggestions">
              <h3>Improvement Suggestions</h3>

              {result.suggestions?.length > 0 ? (
                <ul>
                  {result.suggestions.map((suggestion, index) => (
                    <li key={index}>{suggestion}</li>
                  ))}
                </ul>
              ) : (
                <p>No suggestions returned by the backend.</p>
              )}
            </div>
          </section>
        )}
      </main>

      <footer>ResumeAI · Built with React and Spring Boot</footer>
    </div>
  );
}

export default App;
