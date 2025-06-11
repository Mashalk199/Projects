import {
  Box,
  Heading,
  VStack,
  Text,
  Divider,
  Stack,
  Badge,
  Flex,
  FormControl,
  FormLabel,
  Input,
  Select,
  Button,
  Textarea,
  useToast,
} from "@chakra-ui/react";
import { useEffect, useState, ChangeEvent } from "react";
import { useAuth } from "@/context/AuthContext";
import { Tutor } from "@/types/tutors";
import { useApplications } from "@/context/ApplicationContext";
import { useCourses } from "@/context/CourseContext";
import { useRouter } from "next/router";
import { Application } from "@/types/applications";

export default function ProfilePage() {
  const { user, tutors, setTutors } = useAuth();
  const { applications, setApplications } = useApplications();
  const { courses } = useCourses();

  const [currTutorApps, setCurrTutorApps] = useState<Application[]>([]);

  const [profile, setProfile] = useState<Tutor | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formState, setFormState] = useState({
    availability: "",
    skillsList: "",
    aCredentials: "",
    prevRolesList: "",
  });
  const toast = useToast();
  const router = useRouter();

  // Load tutor applications and match tutor profiles
  useEffect(() => {
    var match: Tutor | undefined;
    if (isTutor) {
      // Find the matching tutor based on username from auth context
      match = tutors.find((tutor) => tutor.user_id === user?.user_id);
    } else {
      match = tutors.find((tutor) => tutor.user_id === tutorId);
    }

    if (match) {
      setProfile(match);
      // Pre-fill the form state with current profile data
      setFormState({
        availability: match.availability,
        skillsList: match.skillsList.join(", "),
        aCredentials: match.aCredentials.join(", "),
        prevRolesList: match.prevRolesList.join(", "),
      });
    }
  }, [user]);

  // Handle form field changes
  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    setFormState({ ...formState, [e.target.name]: e.target.value });
  };

  // Save updated tutor profile to local state
  const handleSave = () => {
    if (!profile) return;
    const updatedProfile: Tutor = {
      ...profile,
      availability: formState.availability,
      skillsList: formState.skillsList.split(",").map((s) => s.trim()),
      aCredentials: formState.aCredentials.split(",").map((c) => c.trim()),
      prevRolesList: formState.prevRolesList.split(",").map((r) => r.trim()),
    };
    setProfile(updatedProfile);
    toast({ title: "Profile updated.", status: "success", isClosable: true });
    setIsEditing(false);
    setTutors((prevTutors) =>
      prevTutors.map((tutor) =>
        tutor.user_id === updatedProfile.user_id ? updatedProfile : tutor
      )
    );
    localStorage.setItem("tutors", JSON.stringify(tutors));
  };

  const { tutorId, courseId } = router.query; // Access the tutorId from the query

  // Set applications based on the user type
  useEffect(() => {
    if (isTutor) {
      setCurrTutorApps(
        applications.filter(
          (application) => application.tutor_id === user.user_id
        )
      );
    } else {
      setCurrTutorApps(
        applications.filter(
          (application) =>
            application.tutor_id === tutorId &&
            application.course_id === courseId
        )
      );
    }
  }, [user, applications]);
  const isTutor = user && "aCredentials" in user;

  return (
    <Flex flex="1" justify="center" py={10}>
      <VStack spacing={6} maxWidth="4xl" width="full">
        <Heading size="lg">Profile of {profile?.username}</Heading>

        {/* Profile card */}
        {profile ? (
          <Box p={6} borderWidth={1} borderRadius="md" width="full">
            <Text mb={2}>
              <strong>Username:</strong> {profile.username}
            </Text>

            {/* Editable form if editing is enabled */}
            {isEditing ? (
              <VStack spacing={4} align="stretch">
                <FormControl>
                  <FormLabel>Availability</FormLabel>
                  <Select
                    name="availability"
                    value={formState.availability}
                    onChange={handleChange}
                  >
                    <option value="full">Full-Time</option>
                    <option value="part">Part-Time</option>
                  </Select>
                </FormControl>

                <FormControl>
                  <FormLabel>Skills (comma-separated)</FormLabel>
                  <Input
                    name="skillsList"
                    value={formState.skillsList}
                    onChange={handleChange}
                  />
                </FormControl>

                <FormControl>
                  <FormLabel>Academic Credentials (comma-separated)</FormLabel>
                  <Input
                    name="aCredentials"
                    value={formState.aCredentials}
                    onChange={handleChange}
                  />
                </FormControl>

                <FormControl>
                  <FormLabel>Previous Roles (comma-separated)</FormLabel>
                  <Input
                    name="prevRolesList"
                    value={formState.prevRolesList}
                    onChange={handleChange}
                  />
                </FormControl>

                <Button colorScheme="green" onClick={handleSave}>
                  Save
                </Button>
              </VStack>
            ) : (
              // View-only profile info
              <VStack align="stretch" spacing={2}>
                <Text>
                  <strong>Availability:</strong>{" "}
                  {profile.availability === "full" ? "Full-Time" : "Part-Time"}
                </Text>
                <Text>
                  <strong>Skills:</strong>{" "}
                  {profile.skillsList.length > 0
                    ? profile.skillsList.join(", ")
                    : "N/A"}
                </Text>
                <Text>
                  <strong>Credentials:</strong>{" "}
                  {profile.aCredentials.join(", ")}
                </Text>
                {profile.prevRolesList.length > 0 && (
                  <Text>
                    <strong>Previous Roles:</strong>{" "}
                    {profile.prevRolesList.join(", ")}
                  </Text>
                )}
                {/* Only displays button if user is tutor, else only displays information for lecturer */}
                {isTutor ? (
                  <Button mt={4} onClick={() => setIsEditing(true)}>
                    Edit
                  </Button>
                ) : null}
              </VStack>
            )}
          </Box>
        ) : (
          <Text>No profile information available.</Text>
        )}

        {/* Applications Section */}
        <Heading size="md" pt={4} alignSelf="start">
          Submitted Applications{" "}
          {!isTutor
            ? "for " +
              courses.find((course) => course.course_id === courseId)
                ?.course_name
            : null}
        </Heading>

        {/* Applications list */}
        {currTutorApps?.length === 0 ? (
          <Text>No applications found.</Text>
        ) : (
          <VStack width="full" spacing={4}>
            {currTutorApps.map((app, idx) => (
              <Box
                key={idx}
                p={4}
                borderWidth={1}
                borderRadius="md"
                width="full"
              >
                <Stack spacing={2}>
                  <Text fontWeight="bold">
                    {app.course_id} -{" "}
                    {
                      courses.find(
                        (course) => course.course_id === app.course_id
                      )?.course_name
                    }
                  </Text>
                  <Badge colorScheme="blue" width="fit-content">
                    {app.role === "lab" ? "Lab Assistant" : "Tutor"}
                  </Badge>

                  <Text>
                    <strong>Cover Message:</strong> {app.message}
                  </Text>
                </Stack>
              </Box>
            ))}
          </VStack>
        )}
      </VStack>
    </Flex>
  );
}
