import { Button, Flex } from "@chakra-ui/react";
import { useAuth } from "@/context/AuthContext";
import Link from "next/link";
import { Tutor } from "../types/tutors";

export default function Navbar() {
  const { user, logout } = useAuth();

  return (
    <Flex as="ul" p={4} bg="blue" color="white" justify="space-between">
      <li>
        <strong>TeachTeam</strong>
      </li>
      <Flex gap={4}>
        <Link href="/">Home</Link>
        {user && <Link href="/Courses">View Courses</Link>}
        <Link href="/Signup">Sign Up</Link>
      </Flex>
      {user ? (
        <Flex gap={4}>
          <Link href="/Profiles">Welcome, {user.username}</Link>
          <Link href="/" onClick={logout}>
            Sign Out
          </Link>
        </Flex>
      ) : (
        <Link href="/Login">Sign In</Link>
      )}
    </Flex>
  );
}
