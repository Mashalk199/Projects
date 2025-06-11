import { Course } from "./courses";

export type Tutor = {
  user_id: string;
  username: string;
  password: string;
  prevRolesList: string[];
  availability: string;
  skillsList: string[];
  aCredentials: string[];
};

export const DEFAULT_TUTORS: Tutor[] = [
  {
    user_id: "1",
    username: "john",
    password: "Password123)",
    prevRolesList: ["Lab Assistant for COSC1111"],
    availability: "full",
    skillsList: ["Python", "Java", "Debugging"],
    aCredentials: ["Masters Degree", "TA Training Certificate"],
  },
  {
    user_id: "2",
    username: "jane",
    password: "Password456)",
    prevRolesList: ["Tutor for COSC1010"],
    availability: "part",
    skillsList: ["Algorithms", "Data Structures", "Mentoring"],
    aCredentials: ["Bachelors Degree", "Distinction in Teaching Award"],
  },
  {
    user_id: "3",
    username: "jacob",
    password: "Password789)",
    prevRolesList: ["Student Mentor"],
    availability: "part",
    skillsList: ["JavaScript", "React", "Team Collaboration"],
    aCredentials: ["Bachelors Degree"],
  },
  {
    user_id: "4",
    username: "emily",
    password: "StrongPass321!",
    prevRolesList: ["Lab Assistant for COSC2222", "Tutor for COSC1111"],
    availability: "full",
    skillsList: ["C++", "OOP", "Public Speaking"],
    aCredentials: ["Honours Degree", "Tutoring Excellence Certificate"],
  },
  {
    user_id: "5",
    username: "michael",
    password: "UniSecure!2024",
    prevRolesList: ["Intern Software Developer"],
    availability: "part",
    skillsList: ["Git", "Linux", "System Design"],
    aCredentials: ["Bachelors Degree in CS"],
  },
  {
    user_id: "6",
    username: "sarah",
    password: "TeachMe@456",
    prevRolesList: ["Teaching Assistant for COSC2201"],
    availability: "full",
    skillsList: ["Machine Learning", "Data Analysis", "Presentation"],
    aCredentials: ["Masters in Data Science"],
  },
  {
    user_id: "7",
    username: "david",
    password: "Dav3!Teach",
    prevRolesList: ["Peer Mentor"],
    availability: "part",
    skillsList: ["Python", "Unit Testing", "Communication"],
    aCredentials: ["Diploma in IT", "Bachelors Degree"],
  },
  {
    user_id: "8",
    username: "nina",
    password: "SecureNina123",
    prevRolesList: ["Research Assistant", "COSC2233 Tutor"],
    availability: "full",
    skillsList: ["SQL", "Database Design", "Java"],
    aCredentials: ["Masters in Software Engineering"],
  },
];
