import { useState, useContext, createContext, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import {
  Box,
  VStack,
  Heading,
  Input,
  InputGroup,
  InputRightElement,
  Button,
  Flex,
  Alert,
  AlertIcon,
  AlertDescription,
} from "@chakra-ui/react";
import { statusObj } from "@/types/statusObj";

// interface for controlling form input data
interface LoginContextType {
  password: string;
  loginStatus: statusObj;
  setPassword: React.Dispatch<React.SetStateAction<string>>;
  setLoginStatus: React.Dispatch<React.SetStateAction<statusObj>>;
  setValidPassword: React.Dispatch<React.SetStateAction<boolean>>;
}
const LoginContext = createContext<LoginContextType | undefined>(undefined);

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [validPassword, setValidPassword] = useState(false);

  const [loginLock, setLoginLock] = useState(false);

  const [loginStatus, setLoginStatus] = useState<statusObj>({
    status: undefined,
    desc: "",
  });

  const { login, user } = useAuth();

  // If user is logged in already, don't let them log in
  useEffect(() => {
    if (user) {
      setLoginLock(true);
    }
  }, [user]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validPassword) {
      const success = login(username, password);
      if (success) {
        setLoginStatus({
          status: "success",
          desc: "Successfully logged in! Welcome " + username,
        });
        setLoginLock(true);
      } else {
        setLoginStatus({
          status: "error",
          desc: "Invalid username or password",
        });
      }
    } else if (!loginStatus.status) {
      setLoginStatus({
        status: "error",
        desc: "Invalid username or password",
      });
    }
  };

  return (
    <Flex as="form" flex="1" direction="column" align="center" justify="center">
      {/* Vertically stack the heading, username and password on
      top of each other */}
      <VStack
        maxWidth="md"
        border="1px"
        borderColor="gray.300"
        borderRadius="md"
        p={10}
      >
        <Heading size="md">Enter details:</Heading>
        {loginStatus.status && (
          <Alert status={loginStatus.status} variant="left-accent">
            <AlertIcon />
            <AlertDescription>{loginStatus.desc}</AlertDescription>
          </Alert>
        )}

        <Input
          placeholder="Username"
          size="md"
          onChange={(e) => setUsername(e.target.value)}
          value={username}
        />
        {/* Pass state to the password field component, so it controls
        user input */}
        <LoginContext.Provider
          value={{
            password,
            loginStatus,
            setPassword,
            setLoginStatus,
            setValidPassword,
          }}
        >
          <PasswordField />
        </LoginContext.Provider>
        <Button
          colorScheme="blue"
          onClick={handleSubmit}
          isDisabled={loginLock ? true : false}
        >
          Log in
        </Button>
      </VStack>
    </Flex>
  );
}

function PasswordField() {
  // Use context to access password, setPassword and setLoginStatus
  const context = useContext(LoginContext);

  if (!context) {
    throw new Error(
      "PasswordField must be used within a LoginContext.Provider"
    );
  }
  const {
    password,
    loginStatus,
    setPassword,
    setLoginStatus,
    setValidPassword,
  } = context;

  const passwordValidation = (userPass: string) => {
    // Set loginStatus to undefined as new character is typed, making the alert disappear
    setLoginStatus({
      ...loginStatus,
      status: undefined,
    });
    // Initially set valid password as false, to remove alert until needed
    setValidPassword(false);
    setPassword(userPass);
    // Checks length is at least 8
    if (userPass.length < 8) {
      setLoginStatus({
        status: "error",
        desc: "Password must be at least 8 characters long.",
      });
    }
    // Checks for at least 1 capital letter
    else if (!/[A-Z]/.test(userPass)) {
      setLoginStatus({
        status: "error",
        desc: "Password must contain at least 1 capital letter.",
      });
    }
    // Checks for at least 1 number
    else if (!/[0-9]/.test(userPass)) {
      setLoginStatus({
        status: "error",
        desc: "Password must contain at least 1 number.",
      });
    }
    // Checks for at least 1 special character
    else if (!/[^\d\s\w]/.test(userPass)) {
      setLoginStatus({
        status: "error",
        desc: "Password must contain at least 1 special character.",
      });
    }
    // Password field has a valid formatted password
    else {
      setValidPassword(true);
    }
  };
  // Implementation for show/hide button
  const [show, setShow] = useState(false);
  const handleClick = () => setShow((show) => !show);
  return (
    <InputGroup size="md">
      <Input
        placeholder="Password"
        type={show ? "text" : "password"}
        onChange={(e) => passwordValidation(e.target.value)}
        value={password}
      />
      <InputRightElement width="4.5rem">
        <Button height="2rem" size="sm" onClick={handleClick}>
          {show ? "Hide" : "Show"}
        </Button>
      </InputRightElement>
    </InputGroup>
  );
}
